from flask import Flask, request, jsonify
from flask_cors import CORS
from sentence_transformers import SentenceTransformer, util
import pandas as pd
import requests

app = Flask(__name__)
CORS(app)  # Allows requests from all origins

# Load sentence transformer model
model = SentenceTransformer('all-MiniLM-L6-v2')

@app.route('/recommend-posts', methods=['POST'])
def recommend_posts():
    try:
        # Get the user input
        data = request.get_json()
        print("RAW incoming data:", data)
        user_input = data.get("user_post", "")

        if not user_input:
            return jsonify({"error": "Missing user_post"}), 400

        # Fetch posts from your Java backend
        backend_response = requests.get("http://localhost:8222/api/v1/posts")
        if backend_response.status_code != 200:
            return jsonify({"error": "Failed to fetch posts from backend"}), 500

        post_list = backend_response.json()

        if not post_list:
            return jsonify({"error": "No posts found in backend"}), 404

        # Create DataFrame
        df = pd.DataFrame(post_list)
        df.fillna('', inplace=True)

        # Ensure required columns exist
        if 'title' not in df or 'content' not in df:
            return jsonify({"error": "Posts must contain 'title' and 'content' fields"}), 500

        # Prepare embeddings
        all_texts = df['title'] + " " + df['content']
        embeddings = model.encode(all_texts.tolist(), convert_to_tensor=True)
        user_embedding = model.encode(user_input, convert_to_tensor=True)

        # Compute similarity
        similarities = util.cos_sim(user_embedding, embeddings)[0].cpu().numpy()
        df['similarity'] = similarities

        # Filter based on threshold
        threshold = 0.25
        df = df[df['similarity'] > threshold]

        if df.empty:
            return jsonify({"error": "No relevant recommendations found"}), 404

        # Return top 3 matches
        top_matches = df.sort_values(by='similarity', ascending=False).head(3)
        return jsonify(top_matches.to_dict(orient='records'))

    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.run(port=5122)

from flask import Flask, request, jsonify
import requests

app = Flask(__name__)

OLLAMA_API_URL = "http://localhost:11434/api/generate"

@app.route('/generate', methods=['POST'])
def generate_post():
    data = request.get_json()
    community_name = data.get('communityName', '')

    if not community_name:
        return jsonify({"error": "Missing community name"}), 400

    # Simple prompt for a short phrase post
    prompt = f"Write a short and engaging sentence about the '{community_name}' community. Keep it simple and engaging."

    response = requests.post(OLLAMA_API_URL, json={
        "model": "deepseek-coder:6.7b-base",
        "prompt": prompt,
        "stream": False
    })

    if response.status_code == 200:
        result = response.json()
        post = result.get("response", "").strip()
        post = post[:255]
        return jsonify({"post": post})
    else:
        return jsonify({"error": "Failed to generate content"}), 500

if __name__ == '__main__':
    app.run(debug=True)

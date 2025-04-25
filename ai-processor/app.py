from flask import Flask, request, jsonify
from flask_cors import CORS
from generator import generate_roadmap

app = Flask(__name__)
CORS(app)

@app.route("/generate-roadmap", methods=["POST"])
def generate():
    data = request.json
    description = data.get("description", "")
    general_tasks = data.get("generalTasks", [])


    roadmap = generate_roadmap(description, general_tasks)
    return jsonify(roadmap)

if __name__ == "__main__":
    app.run(port=5001, debug=True)

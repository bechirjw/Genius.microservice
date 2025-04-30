from flask import Flask, request, jsonify
from flask_cors import CORS
import requests
from langdetect import detect  # pip install langdetect

app = Flask(__name__)
CORS(app)

OLLAMA_API_URL = "http://localhost:11434/api/generate"
MODEL_NAME = "deepseek-coder"

def build_prompt(comment: str, language: str) -> str:
    if language == "fr":
        return f"""
Améliore ce commentaire en gardant le même contexte. Reformule-le en un paragraphe plus détaillé, plus explicatif et plus fluide tout en respectant le ton d’origine.

Commentaire d'origine :
\"{comment}\"

Paragraphe amélioré :
"""
    else:
        return f"""
Improve this comment while keeping the same context. Rewrite it into a more detailed, explanatory, and fluent paragraph, while preserving the original tone.

Original comment:
\"{comment}\"

Enhanced paragraph:
"""

def enhance_comment(comment: str) -> str:
    # Detect language (default to English if detection fails)
    try:
        language = detect(comment)
    except Exception:
        language = "fr"

    prompt = build_prompt(comment, language)

    payload = {
        "model": MODEL_NAME,
        "prompt": prompt,
        "stream": False
    }

    response = requests.post(OLLAMA_API_URL, json=payload)
    response.raise_for_status()
    result = response.json()
    return result.get("response", "").strip()

@app.route('/enhance-comment', methods=['POST'])
def enhance():
    try:
        data = request.get_json()
        comment = data.get("comment", "")
        if not comment.strip():
            return jsonify({"error": "Missing or empty comment"}), 400

        enhanced = enhance_comment(comment)
        return jsonify({"enhanced_comment": enhanced})
    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == '__main__':
    app.run(port=5120)

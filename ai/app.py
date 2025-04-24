from flask import Flask, request, jsonify, make_response
import openai
import re

app = Flask(__name__)
openai.api_key = "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"

@app.route("/generate-questions", methods=["POST"])
def generate_questions():
    description = request.json.get("description", "").strip()
    if not description:
        return make_response(jsonify({"error": "Description is required"}), 400)

    # Prompt structuré pour demander des questions avec options et bonne réponse
    prompt = (
        f"You are an expert quiz creator.\n"
        f"Generate 3 multiple-choice questions about: '{description}'.\n"
        f"Each question should have 4 answer choices and mention the correct answer.\n"
        f"Format:\n\n"
        f"Q1: Your question text here?\n"
        f"A. Option A\n"
        f"B. Option B\n"
        f"C. Option C\n"
        f"D. Option D\n"
        f"Answer: Correct answer content\n\n"
        f"Repeat this structure for Q2 and Q3."
    )

    try:
        response = openai.ChatCompletion.create(
            model="gpt-3.5-turbo",
            messages=[{"role": "user", "content": prompt}],
            temperature=0.7,
            max_tokens=700,
        )

        content = response.choices[0].message.content.strip()
        questions = []
        blocks = re.split(r"Q\d+:", content)[1:]  # Skip anything before Q1

        for block in blocks:
            lines = block.strip().split("\n")
            q_text = lines[0].strip()
            options = {line[0]: line[3:].strip() for line in lines[1:5] if re.match(r"[A-D]\.", line)}
            answer_line = next((line for line in lines if line.lower().startswith("answer:")), None)
            correct_answer = answer_line.split(":", 1)[1].strip() if answer_line else ""

            question = {
                "questionText": q_text,
                "optionA": options.get("A", ""),
                "optionB": options.get("B", ""),
                "optionC": options.get("C", ""),
                "optionD": options.get("D", ""),
                "correctOption": correct_answer
            }
            questions.append(question)

        return jsonify({"questions": questions})

    except Exception as e:
        return make_response(jsonify({"error": str(e)}), 500)

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)

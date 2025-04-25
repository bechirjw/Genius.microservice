import requests
import json
import re
import random

def generate_roadmap(description: str, general_tasks: list) -> list:
    prompt = f"""
Tu es une intelligence artificielle spécialisée en gestion de projets.

Génère exactement 4 sous-tâches uniques et variées à chaque appel, à partir de cette description de projet et des tâches générales.

📘 Description du projet :
{description}

🧩 Tâches générales :
{', '.join(general_tasks)}

🔁 Important :
- Ne répète pas les idées d’une tâche à l’autre.
- Sois créatif et propose des sous-tâches différentes à chaque appel, même si les entrées sont similaires.

Chaque sous-tâche doit respecter ce format JSON (sans commentaire ni texte autour) :

{{
  "titre": "une phrase claire et concise",
  "priorite": "High" | "Medium" | "Low",
  "estimation": "1 jour" | "2 jours" | "3 jours" | "faible" | "moyen" | "élevé"
}}

Réponds uniquement avec un tableau JSON contenant ces 4 sous-tâches.
    """

    try:
        response = requests.post(
            "http://localhost:11434/api/generate",
            json={
                "model": "deepseek-coder",
                "prompt": prompt,
                "stream": False
            },
            timeout=60
        )

        raw_output = response.json().get("response", "")
        print("🪵 RAW IA OUTPUT:\n", raw_output)

        # Extrait tous les objets JSON qui contiennent les trois clés
        object_pattern = r'{[^{}]*"titre"[^{}]*"priorite"[^{}]*"estimation"[^{}]*}'
        matches = re.findall(object_pattern, raw_output)

        subtasks = []
        for match in matches:
            try:
                cleaned = match.replace("’", "'").replace("“", '"').replace("”", '"')
                task = json.loads(cleaned)
                if all(k in task for k in ("titre", "priorite", "estimation")):
                    subtasks.append(task)
            except Exception as e:
                print("❌ Erreur parsing:", e)
                continue

        # Shuffle to randomize if more than 4 found
        random.shuffle(subtasks)

        if subtasks:
            return subtasks[:4]
        else:
            return [{
                "titre": "Le serveur est probablement surchargé. Veuillez réessayer.",
                "priorite": "Low",
                "estimation": "non défini"
            }]

    except Exception as e:
        print("❌ Erreur IA :", e)
        return [{
            "titre": "Erreur de connexion à l’IA. Le serveur est peut-être surchargé.",
            "priorite": "Low",
            "estimation": "non défini"
        }]

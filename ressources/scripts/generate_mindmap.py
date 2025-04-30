#version finale a implementer dans back

#  Importations
import fitz  # PyMuPDF
import requests
import json
import yaml
import re

from graphviz import Digraph

# Couleurs pour les noeuds Graphviz
COLORS = ["#1f77b4", "#2ca02c", "#ff7f0e", "#d62728", "#9467bd", "#8c564b", "#e377c2", "#7f7f7f"]

# Upload du PDF

import sys
pdf_path = sys.argv[1]


# Extraction texte du PDF
def extract_text_from_pdf(pdf_path):
    doc = fitz.open(pdf_path)
    return "".join(page.get_text() for page in doc)

text = extract_text_from_pdf(pdf_path)


api_key = "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"
def get_mistral_response(prompt, api_key):
    url = "https://openrouter.ai/api/v1/chat/completions"
    headers = {
        "Authorization": f"Bearer {api_key}",
        "Content-Type": "application/json"
    }
    data = {
        "model": "mistralai/mistral-7b-instruct",
        "messages": [{"role": "user", "content": prompt}]
    }
    response = requests.post(url, headers=headers, json=data)
    response.raise_for_status()
    return response.json()["choices"][0]["message"]["content"]

# Prompt vers le modèle
prompt = (
    f"Voici un texte académique :\n\n{text[:3000]}\n\n"
    "Peux-tu extraire les concepts clés et les organiser en une hiérarchie parent/enfant ? "
    "Retourne le résultat au format JSON, avec des catégories principales et sous-catégories."
)

response_text = get_mistral_response(prompt, api_key)
print("Réponse reçue du modèle")

# 🧹 Extraction et correction JSON
def extract_and_fix_json(text):
    match = re.search(r'```json\s*([\s\S]*?)\s*```', text) or re.search(r'\{[\s\S]*\}', text)
    if not match:
        raise ValueError("Aucun bloc JSON trouvé.")
    json_text = match.group(1) if '```' in match.group(0) else match.group(0)
    json_text = re.sub(r',(\s*[}\]])', r'\1', json_text)
    return json.loads(json_text)

try:
    concept_dict = extract_and_fix_json(response_text)
except Exception as e:
    print(" Erreur lors du parsing JSON :", e)
    concept_dict = {}



#  Construction Graphviz
def build_graphviz(graph, parent, content, depth=0):
    if isinstance(content, dict):
        for key, value in content.items():
            node_id = f"{parent}_{key}".replace(" ", "_")
            color = COLORS[depth % len(COLORS)]
            graph.node(node_id, f"<<B>{key}</B>>", color=color, style="filled", fillcolor=color+"33", shape="box")
            graph.edge(parent, node_id)
            build_graphviz(graph, node_id, value, depth+1)
    elif isinstance(content, list):
        for i, item in enumerate(content):
            node_id = f"{parent}_item{i}"
            color = COLORS[depth % len(COLORS)]
            graph.node(node_id, str(item), color=color, style="filled", fillcolor=color+"33", shape="ellipse")
            graph.edge(parent, node_id)
    else:
        leaf_id = f"{parent}_{str(content)}".replace(" ", "_")
        color = COLORS[depth % len(COLORS)]
        graph.node(leaf_id, str(content), color=color, style="filled", fillcolor=color+"33", shape="ellipse")
        graph.edge(parent, leaf_id)

def generate_colored_graphviz(concept_dict, output_file="mindmap_color"):
    dot = Digraph(comment="Colored Mindmap", format="png")
    dot.attr(rankdir="LR")
    dot.node("root", "Mindmap", shape="star", style="filled", fillcolor="#FFD700", color="#DAA520")
    build_graphviz(dot, "root", concept_dict)
    dot.render(output_file, view=True)
    print(f"Image générée : {output_file}.png")

generate_colored_graphviz(concept_dict, "mindmap_color")

# Export JSON et YAML
with open("concepts.json", "w") as f:
    json.dump(concept_dict, f, indent=2)

with open("concepts.yaml", "w") as f:
    yaml.dump(concept_dict, f, allow_unicode=True)



# mindmap_generator.py
import json
import sys
from pdfminer.high_level import extract_text
from keybert import KeyBERT
import spacy
import networkx as nx

def generate_mindmap(pdf_path):
    text = extract_text(pdf_path)

    # Détection des concepts (exemple simplifié)
    kw_model = KeyBERT()
    keywords = kw_model.extract_keywords(text, top_n=10)
    concepts = [kw[0] for kw in keywords]

    # Construction du graphe
    G = nx.Graph()
    for concept in concepts:
        G.add_node(concept)

    # Exemple de relations (à adapter avec spaCy)
    for i in range(len(concepts) - 1):
        G.add_edge(concepts[i], concepts[i + 1], label="related")

    # Conversion en JSON
    return {
        "nodes": [{"id": n, "label": n} for n in G.nodes],
        "edges": [{"from": u, "to": v, "label": G.edges[u, v]["label"]} for u, v in G.edges]
    }

if __name__ == "__main__":
    pdf_path = sys.argv[1]  # Reçoit le chemin du PDF depuis Spring Boot
    result = generate_mindmap(pdf_path)
    print(json.dumps(result))  # Envoie à Spring Boot
from fastapi import FastAPI, Form
from fastapi.responses import FileResponse
from fastapi.middleware.cors import CORSMiddleware
from image_generator import generate_image
import uuid
import os

app = FastAPI()

# ✅ Autorisation CORS pour Angular
app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:4200"],  # Tu peux utiliser ["*"] en dev uniquement
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.post("/generate-image/")
def generate(prompt: str = Form(...)):
    os.makedirs("generated", exist_ok=True)
    filename = f"generated/{uuid.uuid4()}.png"
    path = generate_image(prompt, filename)
    return FileResponse(path, media_type="image/png")

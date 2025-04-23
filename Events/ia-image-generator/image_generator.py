import torch
from diffusers import StableDiffusionPipeline

pipe = StableDiffusionPipeline.from_pretrained(
    "runwayml/stable-diffusion-v1-5",
    torch_dtype=torch.float32
)
pipe.to("cuda" if torch.cuda.is_available() else "cpu")

def generate_image(prompt: str, output_path: str) -> str:
    image = pipe(prompt).images[0]
    image.save(output_path)
    return output_path

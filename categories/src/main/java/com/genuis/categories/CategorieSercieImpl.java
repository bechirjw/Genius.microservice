package com.genuis.categories;

import com.genuis.categories.client.RessourceClient;
import com.genuis.categories.client.UserClient;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class CategorieSercieImpl implements ICategorieService {

    @Autowired
    private CategorieRepository categorieRepository;

    @Autowired
    private RessourceClient ressourceClient;

    @Autowired

    private  UserClient userClient;


@Override
public Categorie updateLikes(Long idCategorie, Integer likes) {
    Categorie categorie = categorieRepository.findById(idCategorie)
            .orElseThrow(() -> new EntityNotFoundException("Catégorie non trouvée avec l'ID : " + idCategorie));

    categorie.setLikes(likes);
    return categorieRepository.save(categorie);
}

    @Override
    public List<Categorie> retrieveAllCategories() {
        return categorieRepository.findAll();
    }
    @Override
    public CategorieDTO retrieveCategorieWithImage(Long idCategorie) {
        // Récupérer la catégorie depuis la base de données
        Categorie categorie = categorieRepository.findById(idCategorie)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));

        // Convertir l'image en Base64 si elle existe
        CategorieDTO dto = new CategorieDTO();
        dto.setIdCategorie(categorie.getIdCategorie());
        dto.setNomCategorie(categorie.getNomCategorie());
        dto.setDomaine(categorie.getDomaine());
        dto.setDescription(categorie.getDescription());
        dto.setDateCreation(categorie.getDateCreation());
        dto.setLikes(categorie.getLikes());

        if (categorie.getImage() != null) {
            dto.setImage(Base64.getEncoder().encodeToString(categorie.getImage()));
        }

        return dto;
    }

    @Override
    public Categorie retrieveCategorie(Long idCategorie) {
        return categorieRepository.findById(idCategorie).orElse(null);
    }

    @Override
    public FullResources retrieveCategoriewithresources(Long idCategorie) {
        var categorieOptional = categorieRepository.findById(idCategorie);

        if (categorieOptional.isPresent()) {
            var categorie = categorieOptional.get();
            return FullResources.builder()
                    .idCategorie(categorie.getIdCategorie())
                    .nomCategorie(categorie.getNomCategorie())
                    .ressources(ressourceClient.retrieveRessourceByCategorie(idCategorie))
                    .build();
        } else {
            throw new RuntimeException("Categorie not found");
        }
    }

    @Override
    public Categorie addCategorie(Categorie categorie) {
        return categorieRepository.save(categorie);
    }

    @Override
    public void removeCategorie(Long idCategorie) {
        categorieRepository.deleteById(idCategorie);
    }

    @Override
    public Categorie modifyCategorie(Categorie categorie) {
        return categorieRepository.save(categorie);
    }
    @Override
    public Categorie addCategorieWithImage(String nomCategorie, MultipartFile image) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(image.getInputStream());

        // Convertir l’image en JPG
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", outputStream);
        byte[] jpgData = outputStream.toByteArray();

        Categorie categorie = new Categorie();
        categorie.setNomCategorie(nomCategorie);
        categorie.setImage(jpgData);

        return categorieRepository.save(categorie);
    }
}
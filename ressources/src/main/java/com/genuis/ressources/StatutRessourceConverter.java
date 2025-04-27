package com.genuis.ressources;




import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatutRessourceConverter implements AttributeConverter<StatutRessource, String> {

    @Override
    public String convertToDatabaseColumn(StatutRessource statutRessource) {
        if (statutRessource == null) {
            return null;
        }
        return statutRessource.getValue(); // Utilise la méthode getValue() de l'énumération
    }

    @Override
    public StatutRessource convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        return StatutRessource.fromValue(dbData); // Utilise la méthode fromValue() de l'énumération
    }
}
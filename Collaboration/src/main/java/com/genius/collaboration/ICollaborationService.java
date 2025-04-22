package com.genius.collaboration;


import java.util.List;

public interface ICollaborationService {
    List<Collaboration> retrieveAllCollaborations();
    Collaboration retrieveCollaboration(Long collaborationId);
    Collaboration addCollaboration(Collaboration c);
    void removeCollaboration(Long collaborationId);
    Collaboration modifyCollaboration(Collaboration collaboration);
    Collaboration accepterCollaboration(Long id);

    List<Collaboration> findAllCollaborationsByProjet(Long projetId);
}

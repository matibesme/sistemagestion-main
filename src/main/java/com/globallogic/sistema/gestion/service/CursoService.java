package com.globallogic.sistema.gestion.service;

import com.globallogic.sistema.gestion.dto.*;
import com.globallogic.sistema.gestion.dto.curso.CursoCreateDTO;
import com.globallogic.sistema.gestion.dto.curso.CursoResponseDTO;
import com.globallogic.sistema.gestion.exception.NotFoundException;
import com.globallogic.sistema.gestion.exception.NotProfesorDelCursoException;
import com.globallogic.sistema.gestion.model.*;
import com.globallogic.sistema.gestion.repository.CursoRepository;
import com.globallogic.sistema.gestion.repository.TemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CursoService {

    private final CursoRepository repository;
    private final ProfesorService profesorService;
    private final TemaRepository temaRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public CursoResponseDTO createCurso(CursoCreateDTO data){
        Curso newCurso = new Curso();
        newCurso.setNombre(data.nombre());
        newCurso.setDescripcion(data.descripcion());
        newCurso.setEstado(EstadoCurso.DISPONIBLE);
        var profesores = data.profesoresIds().stream()
                .map(profesorService::getById)
                .toList();
        newCurso.agregarProfesores(profesores);
        this.repository.save(newCurso);
        return CursoResponseDTO.fromCurso(newCurso);
    }

    @PreAuthorize("hasRole('PROFESOR')")
    @Transactional
    public CursoResponseDTO updateCurso(UpdateCursoDTO data, Long id, String userEmail){
        if (!this.repository.existsByIdAndProfesoresACargo_User_EmailIgnoreCase(id, userEmail)) {
            throw new NotProfesorDelCursoException("No estás asignado a este curso.");
        }

        Curso curso = this.repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Curso no encontrado"));

        curso.setNombre(data.nombre());
        curso.setDescripcion(data.descripcion());

        // TODO: resolver lo de los criterios

        curso.getEvaluaciones().clear();
        for (ExamenRequestDTO examenDTO : data.evaluaciones()) {
            Examen examen = new Examen();
            examen.setNombre(examenDTO.nombre());
            examen.setCurso(curso);
            curso.addExamen(examen);
        }

        return CursoResponseDTO.fromCurso(curso);
    }

    //TODO: para mi aca hacer otro metodo con rol admin lo hacemos en /api/admin/cursos/{id}

    @PreAuthorize("hasRole('PROFESOR')")
    @Transactional
    public TemaResponseDTO agregarTema(Long cursoId, TemaAddDTO request, String userEmail){

        if (!this.repository.existsByIdAndProfesoresACargo_User_EmailIgnoreCase(cursoId, userEmail)) {
            throw new NotProfesorDelCursoException("No estás asignado a este curso.");
        }

        var curso = getCursoById(cursoId);
        Tema tema = TemaAddDTO.fromDto(request);

        curso.agregarTema(tema);
        return TemaResponseDTO.fromTema(tema);
    }

    @PreAuthorize("hasRole('PROFESOR')")
    @Transactional
    public void deleteTema(Long cursoId, Long temaId, String userEmail) {

        if (!this.repository.existsByIdAndProfesoresACargo_User_EmailIgnoreCase(cursoId, userEmail)) {
            throw new NotProfesorDelCursoException("No estás asignado a este curso.");
        }

        Curso curso = this.getCursoById(cursoId);

        Tema temaAEliminar = curso.getTemas().stream()
                .filter(tema -> tema.getId().equals(temaId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(("Tema no encontrado")));

        curso.getTemas().remove(temaAEliminar);
        this.temaRepository.delete(temaAEliminar);
    }

    public TemarioResponseDTO obtenerTemario (Long cursoId) {
        Curso curso = this.getCursoById(cursoId);

        List<TemaTemarioResponseDTO> temasDTO = curso.getTemas().stream()
                .map(tema -> new TemaTemarioResponseDTO(
                        tema.getTitulo(),
                        tema.getDuracion()
                ))
                .toList();

        return new TemarioResponseDTO(
                curso.getNombre(),
                temasDTO
        );
    }

    private Curso getCursoById(Long id){
        return this.repository.findById(id).orElseThrow(()-> new NotFoundException("Curso no encontrado"));
    }

}

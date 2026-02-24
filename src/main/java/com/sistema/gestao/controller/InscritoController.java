package com.sistema.gestao.controller;

import com.sistema.gestao.entity.Inscrito;
import com.sistema.gestao.entity.Turma;
import com.sistema.gestao.repository.InscritoRepository;
import com.sistema.gestao.repository.TurmaRepository;
import com.sistema.gestao.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/inscritos")
public class InscritoController {

    @Autowired
    private InscritoRepository inscritoRepository;

    @Autowired
    private TurmaRepository turmaRepository;

    @Autowired
    private PdfService pdfService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("lista", inscritoRepository.findAll());
        return "lista_inscritos";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("inscrito", new Inscrito());
        model.addAttribute("listaTurmas", turmaRepository.findAll());
        return "formulario";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid Inscrito inscrito,
                         BindingResult result,
                         @RequestParam(required = false) List<Long> turmasSelecionadas,
                         Model model,
                         RedirectAttributes attributes) {

        if (result.hasErrors()) {
            model.addAttribute("listaTurmas", turmaRepository.findAll());
            return "formulario";
        }

        // --- VERIFICAÇÃO DE CPF DUPLICADO ---
        if (inscrito.getCpf() != null && !inscrito.getCpf().isEmpty()) {
            Optional<Inscrito> existente = inscritoRepository.findAll().stream()
                    .filter(i -> inscrito.getCpf().equals(i.getCpf()) && !i.getId().equals(inscrito.getId()))
                    .findFirst();

            if (existente.isPresent()) {
                model.addAttribute("erroRegra", "Este CPF já está cadastrado para o aluno: " + existente.get().getNomeCompleto());
                model.addAttribute("listaTurmas", turmaRepository.findAll());
                return "formulario";
            }
        }

        List<Turma> novasTurmas = new ArrayList<>();
        if (turmasSelecionadas != null && !turmasSelecionadas.isEmpty()) {
            novasTurmas = turmaRepository.findAllById(turmasSelecionadas);
        }

        if (novasTurmas.size() > 2) {
            model.addAttribute("erroRegra", "Não é permitido se inscrever em mais de 2 turmas.");
            model.addAttribute("listaTurmas", turmaRepository.findAll());
            return "formulario";
        }

        if (novasTurmas.size() == 2) {
            String mod1 = novasTurmas.get(0).getModalidade();
            String mod2 = novasTurmas.get(1).getModalidade();
            if (mod1 != null && mod1.equalsIgnoreCase(mod2)) {
                model.addAttribute("erroRegra", "Para fazer duas atividades, deve ser uma de FUTEBOL e uma de FUTSAL.");
                model.addAttribute("listaTurmas", turmaRepository.findAll());
                return "formulario";
            }
        }

        inscrito.setTurmas(novasTurmas);
        inscritoRepository.save(inscrito);

        attributes.addFlashAttribute("mensagem", "Beneficiário salvo com sucesso!");
        return "redirect:/inscritos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Optional<Inscrito> inscrito = inscritoRepository.findById(id);
        if (inscrito.isPresent()) {
            model.addAttribute("inscrito", inscrito.get());
            model.addAttribute("listaTurmas", turmaRepository.findAll());
            return "formulario";
        }
        return "redirect:/inscritos";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes attributes) {
        inscritoRepository.deleteById(id);
        attributes.addFlashAttribute("mensagem", "Beneficiário removido com sucesso.");
        return "redirect:/inscritos";
    }

    @GetMapping("/pdf/{id}")
    public ResponseEntity<byte[]> gerarFichaPdf(@PathVariable Long id) {
        Optional<Inscrito> inscritoOpt = inscritoRepository.findById(id);
        if (inscritoOpt.isPresent()) {
            byte[] pdfBytes = pdfService.gerarFichaInscricao(inscritoOpt.get());
            String nomeArquivo = "Ficha_" + inscritoOpt.get().getNomeCompleto().replace(" ", "_") + ".pdf";
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + nomeArquivo)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        }
        return ResponseEntity.notFound().build();
    }
}
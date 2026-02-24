package com.sistema.gestao.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Inscrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- DADOS PESSOAIS E CONTATO ---
    @NotBlank(message = "O nome é obrigatório")
    private String nomeCompleto;

    private String cpf;

    @NotBlank(message = "O sexo é obrigatório")
    private String sexo;

    @NotNull(message = "A data de nascimento é obrigatória")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;

    @NotBlank(message = "O telefone é obrigatório")
    private String telefone;

    @Email(message = "Por favor, insira um e-mail válido (exemplo: nome@dominio.com)")
    private String email;

    // --- ENDEREÇO ---
    @NotBlank(message = "O endereço é obrigatório")
    private String endereco;

    @NotBlank(message = "O bairro é obrigatório")
    private String bairro;

    @NotBlank(message = "A cidade é obrigatória")
    private String cidade;

    // --- INFORMAÇÕES DE SAÚDE ---
    private Double peso;

    private Double altura;

    private String pressaoArterial;

    private String usaMedicamentoContinuo;

    private String medicamentosContinuos;

    // --- ANÁLISE SOCIAL ---
    private String recebeBeneficio;

    private String rendaFamiliar;

    private Integer quantasPessoasCasa;

    // --- MATRÍCULA (MUITOS PARA MUITOS) ---
    @ManyToMany
    @JoinTable(
            name = "inscrito_turmas",
            joinColumns = @JoinColumn(name = "inscrito_id"),
            inverseJoinColumns = @JoinColumn(name = "turma_id")
    )
    @JsonIgnore
    private List<Turma> turmas = new ArrayList<>();

    private String observacoes;

    // --- CAMPOS DE CONTROLE ---
    private boolean ativo = true;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataPreenchimento;

    private String lancadoPor;

    // --- RELACIONAMENTOS ---
    @OneToMany(mappedBy = "inscrito", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Frequencia> frequencias = new ArrayList<>();

    // =================================================================
    // GETTERS E SETTERS
    // =================================================================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }

    public Double getAltura() { return altura; }
    public void setAltura(Double altura) { this.altura = altura; }

    public String getPressaoArterial() { return pressaoArterial; }
    public void setPressaoArterial(String pressaoArterial) { this.pressaoArterial = pressaoArterial; }

    public String getUsaMedicamentoContinuo() { return usaMedicamentoContinuo; }
    public void setUsaMedicamentoContinuo(String usaMedicamentoContinuo) { this.usaMedicamentoContinuo = usaMedicamentoContinuo; }

    public String getMedicamentosContinuos() { return medicamentosContinuos; }
    public void setMedicamentosContinuos(String medicamentosContinuos) { this.medicamentosContinuos = medicamentosContinuos; }

    public String getRecebeBeneficio() { return recebeBeneficio; }
    public void setRecebeBeneficio(String recebeBeneficio) { this.recebeBeneficio = recebeBeneficio; }

    public String getRendaFamiliar() { return rendaFamiliar; }
    public void setRendaFamiliar(String rendaFamiliar) { this.rendaFamiliar = rendaFamiliar; }

    public Integer getQuantasPessoasCasa() { return quantasPessoasCasa; }
    public void setQuantasPessoasCasa(Integer quantasPessoasCasa) { this.quantasPessoasCasa = quantasPessoasCasa; }

    public List<Turma> getTurmas() { return turmas; }
    public void setTurmas(List<Turma> turmas) { this.turmas = turmas; }

    public void adicionarTurma(Turma turma) {
        if (!this.turmas.contains(turma)) {
            this.turmas.add(turma);
        }
    }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public LocalDate getDataPreenchimento() { return dataPreenchimento; }
    public void setDataPreenchimento(LocalDate dataPreenchimento) { this.dataPreenchimento = dataPreenchimento; }

    public String getLancadoPor() { return lancadoPor; }
    public void setLancadoPor(String lancadoPor) { this.lancadoPor = lancadoPor; }

    public List<Frequencia> getFrequencias() { return frequencias; }
    public void setFrequencias(List<Frequencia> frequencias) { this.frequencias = frequencias; }
}
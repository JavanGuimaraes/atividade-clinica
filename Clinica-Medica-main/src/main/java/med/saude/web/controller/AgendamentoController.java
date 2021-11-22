package med.saude.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import med.saude.domain.Agendamento;
import med.saude.domain.Especialidade;
import med.saude.domain.Medico;
import med.saude.domain.Paciente;
import med.saude.service.AgendamentoService;
import med.saude.service.EspecialidadeService;
import med.saude.service.MedicoService;
import med.saude.service.PacienteService;

@Controller
@RequestMapping("/agendamentos")
public class AgendamentoController {
	
	@Autowired
	private AgendamentoService agendamentoService;
	@Autowired
    private EspecialidadeService especialidadeService;
    @Autowired
    private MedicoService medicoService;
    @Autowired
    private PacienteService pacienteService;

	@GetMapping("/cadastrar")
    public String cadastrar(ModelMap model) {
        Agendamento agenda = new Agendamento();
        List<Medico> medico = medicoService.buscarTodos();
        List<Paciente> paciente = pacienteService.buscarTodos();
        List<Especialidade> especialidade = especialidadeService.buscarTodos();    
        model.addAttribute("agendamento", agenda);
        model.addAttribute("medicos", medico);
        model.addAttribute("especialidades", especialidade); 
        model.addAttribute("pacientes", paciente);  
        return "/agendamento/cadastro";
    }
	
	@GetMapping("/listar")
	public String listar(ModelMap model) {
		model.addAttribute("agendamentos", agendamentoService.buscarTodos());
		return "/agendamento/lista";
	}
	
	@PostMapping("/salvar")
    public String salvar(@ModelAttribute Agendamento agendamento,  BindingResult bires) {
        Medico medical = new Medico();
        Especialidade especiality = new Especialidade();
        Paciente paciente = new Paciente();
        medical.setId( Long.valueOf(bires.getFieldValue("medicos").toString()));
        especiality.setId( Long.valueOf(bires.getFieldValue("especialidades").toString()));
        paciente.setId( Long.valueOf(bires.getFieldValue("pacientes").toString()));
        agendamento.setMedicos(medical);
        agendamento.setEspecialidades(especiality);
        agendamento.setPacientes(paciente);
        agendamentoService.salvar(agendamento);
        return "redirect:/agendamentos/cadastrar";
    }

	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("agendamento", agendamentoService.buscarPorId(id));
		return "/agendamento/cadastro";
	}
	
	@PostMapping("/editar")
	public String editar(Agendamento agendamento) {
		agendamentoService.editar(agendamento);
		return "redirect:/agendamentos/listar";
	}
	
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, ModelMap model) {
		agendamentoService.excluir(id);
		return listar(model);	 
	}
}
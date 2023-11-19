package it.tecninf.hrmanagement.rest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.LockModeType;
import javax.persistence.ManyToMany;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.node.ObjectNode;

import it.tecninf.hrmanagement.dto.DipendenteDto;
import it.tecninf.hrmanagement.model.Curriculum;
import it.tecninf.hrmanagement.model.Dipendente;
import it.tecninf.hrmanagement.model.Tipskill;
import it.tecninf.hrmanagement.service.CompetenzeService;
import it.tecninf.hrmanagement.service.CurriculumService;
import it.tecninf.hrmanagement.service.DipendenteService;
import it.tecninf.hrmanagement.utility.BaseResponseDipendente;
import it.tecninf.hrmanagement.utility.ResponseCurricculum;
import it.tecninf.hrmanagement.utility.Utility;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("dipendente")
public class DipendenteController {

	@Autowired
	private DipendenteService dipendenteService;

	@Autowired
	private CurriculumService curriculumService;
	
	@Autowired
	private CompetenzeService competenzeService;
		
	@Autowired
	private Utility utility;

	@GetMapping("/diplist")
	public List<Dipendente> listaDipendenti() {
		return (List<Dipendente>) dipendenteService.listaDipendenti();
	}
	@GetMapping("/dipoldlist")
	public List<Dipendente> listaDipendentiVecchi() {
		return (List<Dipendente>) dipendenteService.listaDipendentiVecchi();
	}

	@GetMapping("/getByID")
	public Dipendente getByID(@RequestParam int id) {
		return dipendenteService.getByID(id).get();
	}

	@GetMapping("/getSkill")
	public List<Dipendente> getSkillFilter(@RequestParam String skill) {
		return (List<Dipendente>) dipendenteService.getSkillFilter(skill);
	}
	
	
	
	
	//------------esecizio 3------------	
	@Transactional
	@Modifying
	@PostMapping("/aggiungiDipendente")
	public int AggiungiDipendente(@RequestBody Dipendente dipendente) {
		int result;
		try {
			// double x =1/0;
			dipendenteService.addDipendente(dipendente.getMatricola(), dipendente.getNome(), dipendente.getCognome(),
					dipendente.getDataDiNascita(), dipendente.getIndirizzo(), dipendente.getCitta(),
					dipendente.getRefNazionalita().getIdRefNazionalita(), dipendente.getRowExist());

			int lastInsertID = dipendenteService.lastIdDipendente();
			String matricola = dipendente.getNome().substring(0, 3).toUpperCase()
					+ dipendente.getCognome().substring(0, 3).toUpperCase() + String.valueOf(lastInsertID);
			dipendente.setMatricola(matricola);
			dipendenteService.updateDipendente(dipendente.getMatricola(), dipendente.getNome(), dipendente.getCognome(),
					dipendente.getDataDiNascita(), dipendente.getIndirizzo(), dipendente.getCitta(),
					dipendente.getRefNazionalita().getIdRefNazionalita(), lastInsertID);

			result = lastInsertID;
		} catch (Exception e) {
			result = 0;
			System.out.println("---------------->" + e.toString());
			System.out.println("Error---" + e.toString());
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Exception", e);
		}

		return result;
		
//		int lastDip = dipendenteService.lastIdDipendente();
//		Dipendente dip = dipendenteService.getByID(lastDip).get();
//		Set<Curriculum> cur=dip.getCurriculum();
//		for(Curriculum c : cur ) {
//			c.setIdCurriculum(lastDip);
//		}
//		dip.setCurriculum(cur);
//		dipendenteService.addDipendente(dip);
	}
	
	
	
	

	@PutMapping("/modificaDipendente")
	public void modificaDipendente(@RequestBody Dipendente dipendente) {
		System.out.println("Modifica del dipendente!");
		dipendenteService.updateDipendente(dipendente.getMatricola(), dipendente.getNome(), dipendente.getCognome(),
				dipendente.getDataDiNascita(), dipendente.getIndirizzo(), dipendente.getCitta(), 
				dipendente.getRefNazionalita().getIdRefNazionalita(), dipendente.getIdDipendente());
	}

	@PutMapping("/deleteDipendente")
	public void cancellaDipendente(@RequestParam int dipendente_id) {
		System.out.println("Cancellazione del dipendente!");
		dipendenteService.deleteByIdDip(dipendente_id);
	}
	
	
	
	
	//------------esecizio 1------------
	@GetMapping("/esercizio_1")
	public List<DipendenteDto> esercizio_1(@RequestParam String dataInizio,@RequestParam String dataFine,@RequestParam String skill)
	{
		return (List<DipendenteDto>) dipendenteService.esercizio_1(dataInizio,dataFine,skill);
	}
	
	/*
	//------------esecizio 3------------
	ERRORE DI RAGIOAMENTO GENERALE!
	@GetMapping("/esercizio_3")
	public int esercizio_3(@RequestBody Dipendente dipendente,@RequestBody List<Tipskill> skills,@RequestBody List<Curriculum> curriculums)
	{
		int result=this.AggiungiDipendente(dipendente);
		this.aggiungiSkills(skills);
		curriculumService.addCurriculums((Set<Curriculum>) curriculums);
		return result;
	}
	è un approccio sbagliato perchè posso gi aggiungere il dipendeten a partire da questo controller, quello
	che devo cercare di fare è aggiungere le skills e i curriculm a partire da una chiave dipendente
	*/
	
	//------------esecizio 4------------
	@PutMapping("/esercizio_4/{id_dipendente}")
	public String esercizio_4(@PathVariable Integer id_dipendente)
	{
		if (id_dipendente == null)
		{
	        return "\nID missing\n";
	    }
		dipendenteService.esercizio_4(id_dipendente);
		return "\nSuccessfully eraise\n";
	}
}
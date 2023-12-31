package it.tecninf.hrmanagement.rest;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import it.tecninf.hrmanagement.model.Tipskill;
import it.tecninf.hrmanagement.service.TipskillService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("tipskill")
public class TipskillController {

	@Autowired
	private TipskillService tipskillService;
		
	@GetMapping("/listaskill")
	public List<Tipskill> listaSkill() {

		return (List<Tipskill>) tipskillService.listaSkill();
	}	
	
	@DeleteMapping("/cancellaskill")
	public void cancellaSkill(@RequestParam int id_tipskill) {		
		tipskillService.cancellaSkill(id_tipskill);
	}
	
	@PostMapping("/aggiungiskill")
	public void aggiungiSkill(@RequestBody Tipskill tipskill) {
		tipskillService.aggiungiSkill(tipskill);
	}
	
	
	
	
	//------------esecizio 3------------esecizio 5------------
	//partire da le informaioni di un dipendente gi salavto nel db, poi all'inserimento di un curriculum e 
	//e poi per le skill...è di netto più efficace
	@PostMapping("/esercizio_3/addSkillsFromID/{id_dipendente}")
	public String esercizio_3_addSkillsFromID(@PathVariable Integer id_dipendente,@RequestBody List<Tipskill> tipskill)
	{
		if (id_dipendente == null)
		{
	        return "\nID missing\n";
	    }
		if (tipskill == null)
		{
	        return "\nSkills missing\n";
	    }
		tipskillService.esercizio_3_addSkillsFromID(id_dipendente,(Set<Tipskill>) tipskill);//collections Set e HashSet non permettono duplicati			
		return "\nSkills updated\n";
	}
}

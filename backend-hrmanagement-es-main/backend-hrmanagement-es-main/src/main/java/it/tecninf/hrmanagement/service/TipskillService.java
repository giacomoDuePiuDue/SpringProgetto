package it.tecninf.hrmanagement.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.tecninf.hrmanagement.model.Competenze;
import it.tecninf.hrmanagement.model.Curriculum;
import it.tecninf.hrmanagement.model.Dipendente;
import it.tecninf.hrmanagement.model.Tipskill;
import it.tecninf.hrmanagement.repository.CompetenzeRepository;
import it.tecninf.hrmanagement.repository.DipendenteRepository;
import it.tecninf.hrmanagement.repository.TipskillRepository;

@Service
public class TipskillService {
	
	@Autowired
	private TipskillRepository tipskillRepository;
	
	@Autowired
	private DipendenteRepository dipRepository;
	
	@Autowired
	private CompetenzeRepository competenzeRepository;
	
	public List<Tipskill> listaSkill() {
		return (List<Tipskill>) tipskillRepository.listaSkill();
	}
	
	public void cancellaSkill(int id_tip_skill) {
		tipskillRepository.cancellaSkill(id_tip_skill);
	}
	
	public void aggiungiSkill(Tipskill tipskill) {
		tipskillRepository.save(tipskill);
	}
	
	
	
	
	//------------esecizio 3------------esecizio 5------------
	public void esercizio_3_addSkillsFromID(int id_dipendente,Set<Tipskill> skills)
	{	
		//Nota: a dipendente fissato devo far variare le righe di competenze in base ai curriculum e alle skill del dipendente
		Dipendente dipendente=dipRepository.findById(id_dipendente).get();
	    (dipendente.getSkills()).addAll(skills);//Set e HashSet sono collections che non contengono duplicati
	    Set<Curriculum> curriculums=dipendente.getCurriculum();

		for(Curriculum curriculum: curriculums)
	    {
	        for(Tipskill skill:skills)
	        {	
	        	if(!(dipendente.getSkills()).contains(skill))
	        	{
	            	Competenze comodo = new Competenze();
	            	comodo.setIdTipskill(skill.getIdTipskill());
	                comodo.setIdDipendente(dipendente.getIdDipendente());
	                comodo.setIdCurriculum(curriculum.getIdCurriculum());
	                competenzeRepository.save(comodo);	        		
	        	}
	        }
	    }	    
	} 
}

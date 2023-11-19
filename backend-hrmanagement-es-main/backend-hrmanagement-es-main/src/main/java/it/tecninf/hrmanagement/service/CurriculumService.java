package it.tecninf.hrmanagement.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Set;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.tecninf.hrmanagement.dto.DipendenteDto;
import it.tecninf.hrmanagement.model.Curriculum;
import it.tecninf.hrmanagement.model.Dipendente;
import it.tecninf.hrmanagement.repository.CurriculumRepository;
import it.tecninf.hrmanagement.repository.DipendenteRepository;
import it.tecninf.hrmanagement.dto.CurriculumDto;

@Service
public class CurriculumService {
	@Autowired
	private CurriculumRepository repository;
	
	@Autowired
	private DipendenteRepository dipRepository;
	

	public List<Curriculum> findall(){
		List<Curriculum> lista = (List<Curriculum>)repository.findAll();
		
		for(Curriculum c : lista) {
			byte[] base = c.getCurriculum();
			String text = Base64.getEncoder().encodeToString(base);
			Base64.Decoder decoder = Base64.getDecoder();  
			String result  = new String(decoder.decode(text));
			c.setPdfText(result);
		}
		return (List<Curriculum>)repository.findAll();
	}

	
	public void addCurriculums(Set<Curriculum> curriculum) {
		repository.saveAll(curriculum);
	}
	
	
	
	
	//------------esecizio 2------------
	public List<CurriculumDto> esercizio_2(Set<String> skills)
	{
		List<Curriculum> curriculum=repository.esercizio_2(skills);
		List<CurriculumDto> curriculumDto=new ArrayList<CurriculumDto>();
		for(Curriculum c:curriculum)
		{			
			CurriculumDto comodo=new CurriculumDto();
			byte[] base = c.getCurriculum();
			String text = Base64.getEncoder().encodeToString(base);
			Base64.Decoder decoder = Base64.getDecoder();			
			String result  = new String(decoder.decode(text));
			c.setPdfText(result);
			
			//curriculumDto.add(new CurriculumDto(c.getDipendente().getNome(),c.getDipendente().getCognome(),c.getCurriculum()));
			
			comodo.getCurriculum().add(c);			
			comodo.setNome(c.getDipendente().getNome());
			comodo.setCognome(c.getDipendente().getCognome());
		
			curriculumDto.add(comodo);
		}
		return curriculumDto;
	}
	//------------esecizio 3------------
	public void esercizio_3_addCVsFromID(int id_dipendente,Set<MultipartFile> files)
	{
		//Nota: a dipendente fissato devo far variare le righe di curriculum
		Dipendente dipendente=dipRepository.findById(id_dipendente).get();
		for(MultipartFile file:files)
		{
			if(!(dipendente.getCurriculum()).contains(file))
			{
				try
				{
					byte[] originalBytes = Base64.getEncoder().encode(file.getBytes());
					
					Curriculum comodo = new Curriculum();
					comodo.setCurriculum(originalBytes);
					comodo.setDipendente(dipendente);
					
					(dipendente.getCurriculum()).add(repository.save(comodo)); //.save() ritorna l'entit aggiunta al database
					
					//faccio comunque l'update del dipendnete nel caso io non lo aggiunga prima?
					/*dipRepository.updateDipendente(dipendente.getMatricola(),dipendente.getNome(),dipendente.getCognome(),dipendente.getDataDiNascita(),
							dipendente.getIndirizzo(),dipendente.getCitta(),dipendente.getRefNazionalita().getIdRefNazionalita(), id_dipendente);*/
				}catch (Exception e)	{
					System.out.println(e.getMessage()+"\n"+e.getCause());//posso lanciare una ecceione a partire da questa ecceione per wrapping poi devo però racchiudere
					//tra try/catch  la chiamata di questa funione
					//NOTA: il controllo dell'eccezione sorge per richiamare .getBytes() !
				}
			}
		}
	}
}

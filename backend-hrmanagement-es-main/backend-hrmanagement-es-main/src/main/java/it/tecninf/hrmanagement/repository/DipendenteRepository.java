package it.tecninf.hrmanagement.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import it.tecninf.hrmanagement.model.Dipendente;


public interface DipendenteRepository extends CrudRepository<Dipendente, Integer> {
	
	@Query(value = "SELECT last_insert_id();", nativeQuery = true)
	public int lastIdDipendente();

	@Query(value = "SELECT * FROM hrmanagement.dipendente WHERE row_exist=1;", nativeQuery = true)
	public List<Dipendente> findAllEmp();

	@Query(value = "SELECT * FROM hrmanagement.dipendente WHERE row_exist=0 ORDER BY cognome ASC", nativeQuery = true)
	public List<Dipendente> findAllOldEmp();
	
	@Modifying
	@Transactional
	@Query(value="UPDATE hrmanagement.dipendente SET row_exist=0 WHERE id_dipendente=?",nativeQuery=true)
	public void deleteByIdDip(int id_dipendente);
	
	@Query(value = "SELECT * FROM hrmanagement.dipendente INNER JOIN hrmanagement.competenze ON dipendente.id_dipendente = competenze.id_dipendente INNER JOIN hrmanagement.curriculum ON curriculum.id_dipendente = competenze.id_dipendente WHERE competenze.skill = ?", nativeQuery = true)
	public List<Dipendente> getSkillFilter(String skill);
	
	@Query(value = "SELECT * FROM hrmanagement.dipendente INNER JOIN hrmanagement.competenze ON dipendente.id_dipendente = competenze.id_dipendente INNER JOIN hrmanagement.curriculum ON curriculum.id_dipendente = competenze.id_dipendente INNER JOIN hrmanagement.tipskill ON competenze.id_tipskill = tipskill.id_tipskill WHERE hrmanagement.dipendente.id_dipendente = ?", nativeQuery = true)
	public Dipendente getSkillFilterByIdDip(int id_dipendente);

	@Modifying
	@Transactional
	@Query(value = "INSERT INTO hrmanagement.dipendente (matricola, nome, cognome, data_di_nascita, indirizzo, citta, id_ref_nazionalita,row_exist) VALUES (?,?,?,?,?,?,?,?)", nativeQuery = true)
	public void addDipendente(String matricola, String nome, String cognome, Date data_di_nascita,
			String indirizzo, String citta, int id_ref_nazionalita,int row_exist);

	@Modifying
	@Transactional
	@Query(value = "UPDATE hrmanagement.dipendente SET matricola=?, nome=?, cognome=?, data_di_nascita=?, indirizzo=?, citta=?, id_ref_nazionalita=? WHERE id_dipendente=?", nativeQuery = true)
	public void updateDipendente(String matricola, String nome, String cognome, Date data_di_nascita,
			String indirizzo, String citta, int id_ref_nazionalita, int id_dipendente);

	
	
	
	
	//------------esecizio 1------------
	@Query(value = "SELECT * "
			+ "FROM hrmanagement.dipendente "
			+ "INNER JOIN hrmanagement.competenze ON dipendente.id_dipendente = competenze.id_dipendente "
			+ "INNER JOIN hrmanagement.tipskill ON competenze.id_tipskill = tipskill.id_tipskill "
			+ "WHERE dipendente.data_di_nascita BETWEEN :dataInizio AND :dataFine AND "
			+ "tipskill.tipologia_skill = :skill "
			+ "ORDER BY dipendente.cognome ASC;", nativeQuery = true)
	public List<Dipendente> esercizio_1(String dataInizio,String dataFine,String skill);
	//------------esecizio 4------------
	@Query(value = "DELETE "
			+ "FROM hrmanagement.dipendente "
			+ "INNER JOIN hrmanagement.storing ON dipendente.id_dipendente = storing.id_dipendente "
			+ "INNER JOIN hrmanagement.user ON dipendente.id_dipendente = user.id_dipendente "
			+ "INNER JOIN hrmanagement.ruolo ON dipendente.id_dipendente = ruolo.id_dipendente "
			+ "INNER JOIN hrmanagement.ref_nazionalita ON dipendente.id_ref_naionalita = ref_nazionalita.id_ref_nazionalita "
			+ "INNER JOIN hrmanagement.curriculum ON dipendente.id_dipendente = curriculum.id_dipendente "
			+ "INNER JOIN hrmanagement.competenze ON dipendente.id_dipendente = competenze.id_dipendente "
			+ "INNER JOIN hrmanagement.curriculum ON curriculum.id_curriculum = competenze.id_curriculum "
			+ "INNER JOIN hrmanagement.tipskill ON competenze.id_tipskill = tipskill.id_tipskill "			
			+ "WHERE dipendente.id_dipendente=:id_dipendente;", nativeQuery = true)
	public void esercizio_4(int id_dipendente);
	
}

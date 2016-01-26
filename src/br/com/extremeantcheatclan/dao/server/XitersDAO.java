package br.com.extremeantcheatclan.dao.server;

import java.util.List;

import br.com.extremeantcheatclan.entity.Jogador;
import br.com.extremeantcheatclan.entity.PlayersOn;
import br.com.extremeantcheatclan.entity.Versao;

public interface XitersDAO {

	Integer insert(Jogador jogador) throws Exception;
	
	Integer insertPlayerOn(String nome,int usuario_id,int numeroSala,int ligaId) throws Exception;

	boolean getServidorLiberado(int id) throws Exception;

	List<PlayersOn> findAllPlayerOn() throws Exception;
	
	PlayersOn findByUsuarioId(int id) throws Exception;
	
	boolean updateDetalhesPlayerOn(PlayersOn playersOn) throws Exception;

	void deletePlayerOff(Integer id) throws Exception;

	Integer getNumeroPlayerOn();

	Versao getVersaoExtremeAntChet();

	int findComenadosAll();
	
	boolean sendRetornoByComando(byte[] retorno, int tipo,int usuario_id);

	void deleteComandoAntXiterTrom(int id);
	
	Integer getCountSerialHashToPlayerOn(Integer id);

	void playerOnIsLife(int playerOnId);
}

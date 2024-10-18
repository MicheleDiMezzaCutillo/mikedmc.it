package it.mikedmc.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.mikedmc.config.DatabaseConfig;
import it.mikedmc.model.MusicanzaMenu;

public class MusicanzaRepository {

	private static MusicanzaRepository musicanzaRepository;
	
	private MusicanzaRepository() {}
	
	public static MusicanzaRepository getIstance() {
		if (musicanzaRepository==null) {
			musicanzaRepository = new MusicanzaRepository();
		}
		return musicanzaRepository;
	}
	
	public List<MusicanzaMenu> getMenuRowsByUser(String user) {
        List<MusicanzaMenu> menuRows = new ArrayList<>();
        String query = "SELECT id, title, link FROM menu WHERE user = ?";
        try {
            Class.forName(DatabaseConfig.mikedmcJdbcDriver);
	        try (Connection connection = DriverManager.getConnection(DatabaseConfig.mikedmcMusicanzaJdbcUrl, DatabaseConfig.mikedmcJdbcUser, DatabaseConfig.mikedmcJdbcPassword);
	             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	
	            preparedStatement.setString(1, user);
	            ResultSet resultSet = preparedStatement.executeQuery();
	
	            while (resultSet.next()) {
	                int id = resultSet.getInt("id");
	                String title = resultSet.getString("title");
	                String link = resultSet.getString("link");
	                menuRows.add(new MusicanzaMenu(id, title, link));
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            // Gestisci l'eccezione in modo appropriato
	        }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return menuRows;
    }
	
	public boolean deleteById(int id, String userId) {
        String query = "DELETE FROM menu WHERE id = ? AND user = ?";
        boolean isDeleted = false;
                    try {
                    	Class.forName(DatabaseConfig.mikedmcJdbcDriver);
            	        try (Connection connection = DriverManager.getConnection(DatabaseConfig.mikedmcMusicanzaJdbcUrl, DatabaseConfig.mikedmcJdbcUser, DatabaseConfig.mikedmcJdbcPassword);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, userId);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                isDeleted = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestisci l'eccezione in modo appropriato
        }

                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
        return isDeleted;
    }
	
	public int getTotalContatoreByUserId(String userId) {
	    int totalContatore = 0;
	    String query = "SELECT SUM(contatore) AS total FROM logcomandi WHERE userId = ?";
	    
	    try {
	    	Class.forName(DatabaseConfig.mikedmcJdbcDriver);
	        try (Connection connection = DriverManager.getConnection(DatabaseConfig.mikedmcMusicanzaJdbcUrl, DatabaseConfig.mikedmcJdbcUser, DatabaseConfig.mikedmcJdbcPassword);
	             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

	            preparedStatement.setString(1, userId);
	            ResultSet resultSet = preparedStatement.executeQuery();

	            if (resultSet.next()) {
	                totalContatore = resultSet.getInt("total");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            // Gestisci l'eccezione in modo appropriato
	        }
	    } catch (ClassNotFoundException e) {
	        throw new RuntimeException(e);
	    }

	    return totalContatore;
	}

	public String getNomeServerByUserIdWithMaxContatore(String userId) {
	    String nomeServer = null;
	    String query = "SELECT nomeServer FROM logcomandi WHERE userId = ? ORDER BY contatore DESC LIMIT 1";

	    try {
	    	Class.forName(DatabaseConfig.mikedmcJdbcDriver);
	        try (Connection connection = DriverManager.getConnection(DatabaseConfig.mikedmcMusicanzaJdbcUrl, DatabaseConfig.mikedmcJdbcUser, DatabaseConfig.mikedmcJdbcPassword);
	             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

	            preparedStatement.setString(1, userId);
	            ResultSet resultSet = preparedStatement.executeQuery();

	            if (resultSet.next()) {
	                nomeServer = resultSet.getString("nomeServer");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            // Gestisci l'eccezione in modo appropriato
	        }
	    } catch (ClassNotFoundException e) {
	        throw new RuntimeException(e);
	    }

	    return nomeServer;
	}

	public int getTotaleContatoreByUserIdAndNomeServer(String userId, String nomeServer) {
	    int totaleContatore = 0;
	    String query = "SELECT SUM(contatore) AS totale_contatore FROM logcomandi WHERE userId = ? AND nomeServer = ?";

	    try {
	    	Class.forName(DatabaseConfig.mikedmcJdbcDriver);
	        try (Connection connection = DriverManager.getConnection(DatabaseConfig.mikedmcMusicanzaJdbcUrl, DatabaseConfig.mikedmcJdbcUser, DatabaseConfig.mikedmcJdbcPassword);
	             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

	            preparedStatement.setString(1, userId);
	            preparedStatement.setString(2, nomeServer);
	            ResultSet resultSet = preparedStatement.executeQuery();

	            if (resultSet.next()) {
	                totaleContatore = resultSet.getInt("totale_contatore");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            // Gestisci l'eccezione in modo appropriato
	        }
	    } catch (ClassNotFoundException e) {
	        throw new RuntimeException(e);
	    }

	    return totaleContatore;
	}

	
	
	// Metodi per la console ADMIN
	public int getTotaleContatore() {
	    int totaleContatore = 0;
	    String query = "SELECT SUM(contatore) AS totale_contatore FROM logcomandi";

	    try {
	    	Class.forName(DatabaseConfig.mikedmcJdbcDriver);
	        try (Connection connection = DriverManager.getConnection(DatabaseConfig.mikedmcMusicanzaJdbcUrl, DatabaseConfig.mikedmcJdbcUser, DatabaseConfig.mikedmcJdbcPassword);
	             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

	            ResultSet resultSet = preparedStatement.executeQuery();

	            if (resultSet.next()) {
	                totaleContatore = resultSet.getInt("totale_contatore");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            // Gestisci l'eccezione in modo appropriato
	        }
	    } catch (ClassNotFoundException e) {
	        throw new RuntimeException(e);
	    }

	    return totaleContatore;
	}

	
	public int getNumeroNomiDistinti() {
	    int numeroNomiDistinti = 0;
	    String query = "SELECT COUNT(DISTINCT nome) AS numero_nomi_distinti FROM logcomandi";

	    try {
	    	Class.forName(DatabaseConfig.mikedmcJdbcDriver);
	        try (Connection connection = DriverManager.getConnection(DatabaseConfig.mikedmcMusicanzaJdbcUrl, DatabaseConfig.mikedmcJdbcUser, DatabaseConfig.mikedmcJdbcPassword);
	             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

	            ResultSet resultSet = preparedStatement.executeQuery();

	            if (resultSet.next()) {
	                numeroNomiDistinti = resultSet.getInt("numero_nomi_distinti");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            // Gestisci l'eccezione in modo appropriato
	        }
	    } catch (ClassNotFoundException e) {
	        throw new RuntimeException(e);
	    }

	    return numeroNomiDistinti;
	}

	
	public int getNumeroNomiServerDistinti() {
	    int numeroNomiServerDistinti = 0;
	    String query = "SELECT COUNT(DISTINCT nomeServer) AS numero_nomi_server_distinti FROM logcomandi";

	    try {
	    	Class.forName(DatabaseConfig.mikedmcJdbcDriver);
	        try (Connection connection = DriverManager.getConnection(DatabaseConfig.mikedmcMusicanzaJdbcUrl, DatabaseConfig.mikedmcJdbcUser, DatabaseConfig.mikedmcJdbcPassword);
	             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

	            ResultSet resultSet = preparedStatement.executeQuery();

	            if (resultSet.next()) {
	            	numeroNomiServerDistinti = resultSet.getInt("numero_nomi_server_distinti");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            // Gestisci l'eccezione in modo appropriato
	        }
	    } catch (ClassNotFoundException e) {
	        throw new RuntimeException(e);
	    }

	    return numeroNomiServerDistinti;
	}

	public Map<String, Integer> getComandiESommaContatore() {
	    Map<String, Integer> comandiContatoreMap = new HashMap<>();
	    String query = "SELECT comando, SUM(contatore) AS somma_contatore FROM logcomandi GROUP BY comando ORDER BY somma_contatore DESC";

	    try {
	    	Class.forName(DatabaseConfig.mikedmcJdbcDriver);
	        try (Connection connection = DriverManager.getConnection(DatabaseConfig.mikedmcMusicanzaJdbcUrl, DatabaseConfig.mikedmcJdbcUser, DatabaseConfig.mikedmcJdbcPassword);
	             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

	            ResultSet resultSet = preparedStatement.executeQuery();

	            while (resultSet.next()) {
	                String comando = resultSet.getString("comando");
	                int sommaContatore = resultSet.getInt("somma_contatore");
	                comandiContatoreMap.put(comando, sommaContatore);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            // Gestisci l'eccezione in modo appropriato
	        }
	    } catch (ClassNotFoundException e) {
	        throw new RuntimeException(e);
	    }

	    return comandiContatoreMap;
	}

	public Map<String, Integer> getNomeServerESommaContatore() {
	    Map<String, Integer> nomeServerContatoreMap = new HashMap<>();
	    String query = "SELECT nomeServer, SUM(contatore) AS somma_contatore FROM logcomandi GROUP BY nomeServer ORDER BY somma_contatore DESC";

	    try {
	    	Class.forName(DatabaseConfig.mikedmcJdbcDriver);
	        try (Connection connection = DriverManager.getConnection(DatabaseConfig.mikedmcMusicanzaJdbcUrl, DatabaseConfig.mikedmcJdbcUser, DatabaseConfig.mikedmcJdbcPassword);
	             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

	            ResultSet resultSet = preparedStatement.executeQuery();

	            while (resultSet.next()) {
	                String nomeServer = resultSet.getString("nomeServer");
	                int sommaContatore = resultSet.getInt("somma_contatore");
	                nomeServerContatoreMap.put(nomeServer, sommaContatore);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            // Gestisci l'eccezione in modo appropriato
	        }
	    } catch (ClassNotFoundException e) {
	        throw new RuntimeException(e);
	    }

	    return nomeServerContatoreMap;
	}

	public Map<String, Integer> getNomiESommaContatore() {
	    Map<String, Integer> nomiContatoreMap = new HashMap<>();
	    String query = "SELECT nome, SUM(contatore) AS somma_contatore FROM logcomandi GROUP BY nome ORDER BY somma_contatore DESC";

	    try {
	    	Class.forName(DatabaseConfig.mikedmcJdbcDriver);
	        try (Connection connection = DriverManager.getConnection(DatabaseConfig.mikedmcMusicanzaJdbcUrl, DatabaseConfig.mikedmcJdbcUser, DatabaseConfig.mikedmcJdbcPassword);
	             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

	            ResultSet resultSet = preparedStatement.executeQuery();

	            while (resultSet.next()) {
	                String nome = resultSet.getString("nome");
	                int sommaContatore = resultSet.getInt("somma_contatore");
	                nomiContatoreMap.put(nome, sommaContatore);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            // Gestisci l'eccezione in modo appropriato
	        }
	    } catch (ClassNotFoundException e) {
	        throw new RuntimeException(e);
	    }

	    return nomiContatoreMap;
	}

}

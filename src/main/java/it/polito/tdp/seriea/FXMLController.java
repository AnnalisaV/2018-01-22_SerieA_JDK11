package it.polito.tdp.seriea;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.seriea.model.Model;
import it.polito.tdp.seriea.model.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<Team> boxSquadra;

    @FXML
    private Button btnSelezionaSquadra;

    @FXML
    private Button btnTrovaAnnataOro;

    @FXML
    private Button btnTrovaCamminoVirtuoso;

    @FXML
    private TextArea txtResult;

    @FXML
    void doSelezionaSquadra(ActionEvent event) {

    	txtResult.clear();
    	
    	if (boxSquadra.getValue()==null) {
    		txtResult.appendText("ERRORE : Selezionare Team! \n");
    		return; 
    	}
    	
    	Map<Integer, Integer> squadrePunti= model.getSeasonPoints(boxSquadra.getValue());
    	for (Integer i : squadrePunti.keySet()) {
    		txtResult.appendText("Stagione: "+i +" punti: "+squadrePunti.get(i)+"\n");
    	}
    	
    	btnTrovaAnnataOro.setDisable(false);
    }

    @FXML
    void doTrovaAnnataOro(ActionEvent event) {

    	txtResult.clear();
    	
    	//il controllo a' fatto prima altrimenti qui non potrei accedere
    	txtResult.appendText(model.annataDOro(boxSquadra.getValue()));
    	//model.creaGrafo(boxSquadra.getValue()); 
    	
    	this.btnTrovaCamminoVirtuoso.setDisable(false);
    }

    @FXML
    void doTrovaCamminoVirtuoso(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert boxSquadra != null : "fx:id=\"boxSquadra\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnSelezionaSquadra != null : "fx:id=\"btnSelezionaSquadra\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnTrovaAnnataOro != null : "fx:id=\"btnTrovaAnnataOro\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnTrovaCamminoVirtuoso != null : "fx:id=\"btnTrovaCamminoVirtuoso\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'SerieA.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		this.boxSquadra.getItems().addAll(model.getTeams()); 
		this.btnTrovaAnnataOro.setDisable(true);
		this.btnTrovaCamminoVirtuoso.setDisable(true);
	}
}

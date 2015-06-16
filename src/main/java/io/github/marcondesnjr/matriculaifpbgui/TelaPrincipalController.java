/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.marcondesnjr.matriculaifpbgui;

import io.github.marcondesnjr.cadastroaluno.AlunoBuilder;
import io.github.marcondesnjr.cadastroaluno.AlunoInvalidoException;
import io.github.marcondesnjr.cadastroaluno.CadastrarAluno;
import io.github.marcondesnjr.cadastroaluno.DocumentoException;
import io.github.marcondesnjr.namevalidator.NameValidator;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
/**
 * FXML Controller class
 *
 * @author José Marcondes do Nascimento Junior
 */
public class TelaPrincipalController implements Initializable {
    @FXML
    private TextField tfNome;
    @FXML
    private TextField tfCpf;
    @FXML
    private TextField tfRg;
    @FXML
    private TextField tfNacionalidade;
    @FXML
    private TextField tfDocMilitar;
    @FXML
    private RadioButton rbMasculido;
    @FXML
    private ToggleGroup sexo;
    @FXML
    private RadioButton rbFeminino;
    @FXML
    private DatePicker dpDataNasc;
    @FXML
    private ProgressIndicator progressId;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tfDocMilitar.setDisable(true);
        tfNome.focusedProperty().addListener((ObservableValue<? extends Boolean> observable,Boolean oldValue, Boolean newValue) -> {
            if(oldValue){
                if(!NameValidator.isValid(tfNome.getText())){
                    if(!tfNome.getStyleClass().contains("error"))
                        tfNome.getStyleClass().add("error");
                }else{
                    tfNome.getStyleClass().remove("error");
                }
            }
        });
        
        dpDataNasc.focusedProperty().addListener(new DocMilitarLoc());
        sexo.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) -> {
            if(dpDataNasc.getValue() != null){
                if(LocalDate.now().getYear() - dpDataNasc.getValue().getYear() >= 18 &&
                        rbMasculido.isSelected()){
                    tfDocMilitar.setDisable(false);
                }
                else{
                    tfDocMilitar.setDisable(true);
                }
            }
        });
    }    

    @FXML
    private void cancelarAH(ActionEvent event) {
    }

    @FXML
    private void concluirAH(ActionEvent event) {
        progressId.setProgress(-1);
        String nome = tfNome.getText();
        String cpf = tfCpf.getText();
        String Naci = tfNacionalidade.getText();
        String rg = tfRg.getText();
        char sx = rbFeminino.isSelected()?'F':'M';
        LocalDate dtNasc = dpDataNasc.getValue();
        AlunoBuilder alB = new AlunoBuilder(nome, cpf, rg, dtNasc , Naci, sx);
        try {
            if(!tfDocMilitar.getText().equals(""))
                alB.comDocMilitar(tfDocMilitar.getText());
            CadastrarAluno.cadastrarNovo(alB.build());
            progressId.setProgress(1);            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucesso");
            alert.setHeaderText("O aluno foi cadastrado com sucesso");
            alert.showAndWait();
        }catch (DocumentoException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro!");
            alert.setHeaderText("O Aluno não deve possuir um documento militar");
            alert.showAndWait();
        } catch (AlunoInvalidoException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro!");
            alert.setHeaderText(ex.getMessage());
            alert.showAndWait();
        }
    }
    
    private class DocMilitarLoc implements ChangeListener<Boolean>{

        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if(dpDataNasc.getValue() != null){
                if(LocalDate.now().getYear() - dpDataNasc.getValue().getYear() >= 18 &&
                        rbMasculido.isSelected()){
                    tfDocMilitar.setDisable(false);
                }
                else{
                    tfDocMilitar.setDisable(true);
                }
            }
        }
        
    }
    
}

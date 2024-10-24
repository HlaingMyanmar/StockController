package org.saleorderoptions;

import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class NewItemViewController {

    @FXML
    private JFXCheckBox cashchebox;

    @FXML
    private JFXCheckBox kbzchebox;

    @FXML
    private Button newItem;

    @FXML
    private TextField stockdiscount;

    @FXML
    private TextField stockidtxt;

    @FXML
    private TextField stocknametxt;

    @FXML
    private TextField stockpricetxt;

    @FXML
    private TextField stockqtytxt;

    @FXML
    private ComboBox<?> stockwarranty;

    @FXML
    private JFXCheckBox wavechebox;

}

package org.saleorderoptions;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class SaleOrderController {

    @FXML
    private TableColumn<?, ?> codeCol;

    @FXML
    private Label codetxt;

    @FXML
    private Label datetxt;

    @FXML
    private TableColumn<?, ?> discountCol;

    @FXML
    private Button editbtn;

    @FXML
    private TableColumn<?, ?> icon2Col;

    @FXML
    private TableColumn<?, ?> iconCol;

    @FXML
    private TableColumn<?, ?> nameCol;

    @FXML
    private Button newItem;

    @FXML
    private TableColumn<?, ?> priceCol;

    @FXML
    private TableColumn<?, ?> priceCol2;

    @FXML
    private TableColumn<?, ?> qtyCol;

    @FXML
    private TableView<?> saletable;

    @FXML
    private Label stocktotaltxt;

    @FXML
    private TableColumn<?, ?> totalCol;

    @FXML
    private TableColumn<?, ?> warrantyCol;

}

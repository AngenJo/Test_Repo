package frontend;

import backend.Microcontroller;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MemoryController {

    @FXML
    private Label cell00;
    @FXML
    private Label cell01;
    @FXML
    private Label cell02;
    @FXML
    private Label cell03;
    @FXML
    private Label cell04;
    @FXML
    private Label cell05;
    @FXML
    private Label cell06;
    @FXML
    private Label cell07;
    @FXML
    private Label cell08;
    @FXML
    private Label cell09;
    @FXML
    private Label cell0A;
    @FXML
    private Label cell0B;
    @FXML
    private Label cell0C;
    @FXML
    private Label cell0D;
    @FXML
    private Label cell0E;
    @FXML
    private Label cell0F;
    @FXML
    private Label cell10;
    @FXML
    private Label cell11;
    @FXML
    private Label cell12;
    @FXML
    private Label cell13;
    @FXML
    private Label cell14;
    @FXML
    private Label cell15;
    @FXML
    private Label cell16;
    @FXML
    private Label cell17;
    @FXML
    private Label cell18;
    @FXML
    private Label cell19;
    @FXML
    private Label cell1A;
    @FXML
    private Label cell1B;
    @FXML
    private Label cell1C;
    @FXML
    private Label cell1D;
    @FXML
    private Label cell1E;
    @FXML
    private Label cell1F;
    @FXML
    private Label cell20;
    @FXML
    private Label cell21;
    @FXML
    private Label cell22;
    @FXML
    private Label cell23;
    @FXML
    private Label cell24;
    @FXML
    private Label cell25;
    @FXML
    private Label cell26;
    @FXML
    private Label cell27;
    @FXML
    private Label cell28;
    @FXML
    private Label cell29;
    @FXML
    private Label cell2A;
    @FXML
    private Label cell2B;
    @FXML
    private Label cell2C;
    @FXML
    private Label cell2D;
    @FXML
    private Label cell2E;
    @FXML
    private Label cell2F;
    @FXML
    private Label cell30;
    @FXML
    private Label cell31;
    @FXML
    private Label cell32;
    @FXML
    private Label cell33;
    @FXML
    private Label cell34;
    @FXML
    private Label cell35;
    @FXML
    private Label cell36;
    @FXML
    private Label cell37;
    @FXML
    private Label cell38;
    @FXML
    private Label cell39;
    @FXML
    private Label cell3A;
    @FXML
    private Label cell3B;
    @FXML
    private Label cell3C;
    @FXML
    private Label cell3D;
    @FXML
    private Label cell3E;
    @FXML
    private Label cell3F;
    @FXML
    private Label cell40;
    @FXML
    private Label cell41;
    @FXML
    private Label cell42;
    @FXML
    private Label cell43;
    @FXML
    private Label cell44;
    @FXML
    private Label cell45;
    @FXML
    private Label cell46;
    @FXML
    private Label cell47;
    @FXML
    private Label cell48;
    @FXML
    private Label cell49;
    @FXML
    private Label cell4A;
    @FXML
    private Label cell4B;
    @FXML
    private Label cell4C;
    @FXML
    private Label cell4D;
    @FXML
    private Label cell4E;
    @FXML
    private Label cell4F;
    @FXML
    private Label cell50;
    @FXML
    private Label cell51;
    @FXML
    private Label cell52;
    @FXML
    private Label cell53;
    @FXML
    private Label cell54;
    @FXML
    private Label cell55;
    @FXML
    private Label cell56;
    @FXML
    private Label cell57;
    @FXML
    private Label cell58;
    @FXML
    private Label cell59;
    @FXML
    private Label cell5A;
    @FXML
    private Label cell5B;
    @FXML
    private Label cell5C;
    @FXML
    private Label cell5D;
    @FXML
    private Label cell5E;
    @FXML
    private Label cell5F;
    @FXML
    private Label cell60;
    @FXML
    private Label cell61;
    @FXML
    private Label cell62;
    @FXML
    private Label cell63;
    @FXML
    private Label cell64;
    @FXML
    private Label cell65;
    @FXML
    private Label cell66;
    @FXML
    private Label cell67;
    @FXML
    private Label cell68;
    @FXML
    private Label cell69;
    @FXML
    private Label cell6A;
    @FXML
    private Label cell6B;
    @FXML
    private Label cell6C;
    @FXML
    private Label cell6D;
    @FXML
    private Label cell6E;
    @FXML
    private Label cell6F;
    @FXML
    private Label cell70;
    @FXML
    private Label cell71;
    @FXML
    private Label cell72;
    @FXML
    private Label cell73;
    @FXML
    private Label cell74;
    @FXML
    private Label cell75;
    @FXML
    private Label cell76;
    @FXML
    private Label cell77;
    @FXML
    private Label cell78;
    @FXML
    private Label cell79;
    @FXML
    private Label cell7A;
    @FXML
    private Label cell7B;
    @FXML
    private Label cell7C;
    @FXML
    private Label cell7D;
    @FXML
    private Label cell7E;
    @FXML
    private Label cell7F;
    @FXML
    private Label cell80;
    @FXML
    private Label cell81;
    @FXML
    private Label cell82;
    @FXML
    private Label cell83;
    @FXML
    private Label cell84;
    @FXML
    private Label cell85;
    @FXML
    private Label cell86;
    @FXML
    private Label cell87;
    @FXML
    private Label cell88;
    @FXML
    private Label cell89;
    @FXML
    private Label cell8A;
    @FXML
    private Label cell8B;
    @FXML
    private Label cell8C;
    @FXML
    private Label cell8D;
    @FXML
    private Label cell8E;
    @FXML
    private Label cell8F;
    @FXML
    private Label cell90;
    @FXML
    private Label cell91;
    @FXML
    private Label cell92;
    @FXML
    private Label cell93;
    @FXML
    private Label cell94;
    @FXML
    private Label cell95;
    @FXML
    private Label cell96;
    @FXML
    private Label cell97;
    @FXML
    private Label cell98;
    @FXML
    private Label cell99;
    @FXML
    private Label cell9A;
    @FXML
    private Label cell9B;
    @FXML
    private Label cell9C;
    @FXML
    private Label cell9D;
    @FXML
    private Label cell9E;
    @FXML
    private Label cell9F;
    @FXML
    private Label cellA0;
    @FXML
    private Label cellA1;
    @FXML
    private Label cellA2;
    @FXML
    private Label cellA3;
    @FXML
    private Label cellA4;
    @FXML
    private Label cellA5;
    @FXML
    private Label cellA6;
    @FXML
    private Label cellA7;
    @FXML
    private Label cellA8;
    @FXML
    private Label cellA9;
    @FXML
    private Label cellAA;
    @FXML
    private Label cellAB;
    @FXML
    private Label cellAC;
    @FXML
    private Label cellAD;
    @FXML
    private Label cellAE;
    @FXML
    private Label cellAF;
    @FXML
    private Label cellB0;
    @FXML
    private Label cellB1;
    @FXML
    private Label cellB2;
    @FXML
    private Label cellB3;
    @FXML
    private Label cellB4;
    @FXML
    private Label cellB5;
    @FXML
    private Label cellB6;
    @FXML
    private Label cellB7;
    @FXML
    private Label cellB8;
    @FXML
    private Label cellB9;
    @FXML
    private Label cellBA;
    @FXML
    private Label cellBB;
    @FXML
    private Label cellBC;
    @FXML
    private Label cellBD;
    @FXML
    private Label cellBE;
    @FXML
    private Label cellBF;
    @FXML
    private Label cellC0;
    @FXML
    private Label cellC1;
    @FXML
    private Label cellC2;
    @FXML
    private Label cellC3;
    @FXML
    private Label cellC4;
    @FXML
    private Label cellC5;
    @FXML
    private Label cellC6;
    @FXML
    private Label cellC7;
    @FXML
    private Label cellC8;
    @FXML
    private Label cellC9;
    @FXML
    private Label cellCA;
    @FXML
    private Label cellCB;
    @FXML
    private Label cellCC;
    @FXML
    private Label cellCD;
    @FXML
    private Label cellCE;
    @FXML
    private Label cellCF;
    @FXML
    private Label cellD0;
    @FXML
    private Label cellD1;
    @FXML
    private Label cellD2;
    @FXML
    private Label cellD3;
    @FXML
    private Label cellD4;
    @FXML
    private Label cellD5;
    @FXML
    private Label cellD6;
    @FXML
    private Label cellD7;
    @FXML
    private Label cellD8;
    @FXML
    private Label cellD9;
    @FXML
    private Label cellDA;
    @FXML
    private Label cellDB;
    @FXML
    private Label cellDC;
    @FXML
    private Label cellDD;
    @FXML
    private Label cellDE;
    @FXML
    private Label cellDF;
    @FXML
    private Label cellE0;
    @FXML
    private Label cellE1;
    @FXML
    private Label cellE2;
    @FXML
    private Label cellE3;
    @FXML
    private Label cellE4;
    @FXML
    private Label cellE5;
    @FXML
    private Label cellE6;
    @FXML
    private Label cellE7;
    @FXML
    private Label cellE8;
    @FXML
    private Label cellE9;
    @FXML
    private Label cellEA;
    @FXML
    private Label cellEB;
    @FXML
    private Label cellEC;
    @FXML
    private Label cellED;
    @FXML
    private Label cellEE;
    @FXML
    private Label cellEF;
    @FXML
    private Label cellF0;
    @FXML
    private Label cellF1;
    @FXML
    private Label cellF2;
    @FXML
    private Label cellF3;
    @FXML
    private Label cellF4;
    @FXML
    private Label cellF5;
    @FXML
    private Label cellF6;
    @FXML
    private Label cellF7;
    @FXML
    private Label cellF8;
    @FXML
    private Label cellF9;
    @FXML
    private Label cellFA;
    @FXML
    private Label cellFB;
    @FXML
    private Label cellFC;
    @FXML
    private Label cellFD;
    @FXML
    private Label cellFE;
    @FXML
    private Label cellFF;

    private Microcontroller mc;
    private Label[] memLabels;

    public void setMicrocontroller(Microcontroller mc) {
        this.mc = mc;
    }

    public void init() {
        this.memLabels = new Label[]{
                cell00, cell01, cell02, cell03, cell04, cell05, cell06, cell07, cell08, cell09, cell0A, cell0B, cell0C, cell0D, cell0E, cell0F,
                cell10, cell11, cell12, cell13, cell14, cell15, cell16, cell17, cell18, cell19, cell1A, cell1B, cell1C, cell1D, cell1E, cell1F,
                cell20, cell21, cell22, cell23, cell24, cell25, cell26, cell27, cell28, cell29, cell2A, cell2B, cell2C, cell2D, cell2E, cell2F,
                cell30, cell31, cell32, cell33, cell34, cell35, cell36, cell37, cell38, cell39, cell3A, cell3B, cell3C, cell3D, cell3E, cell3F,
                cell40, cell41, cell42, cell43, cell44, cell45, cell46, cell47, cell48, cell49, cell4A, cell4B, cell4C, cell4D, cell4E, cell4F,
                cell50, cell51, cell52, cell53, cell54, cell55, cell56, cell57, cell58, cell59, cell5A, cell5B, cell5C, cell5D, cell5E, cell5F,
                cell60, cell61, cell62, cell63, cell64, cell65, cell66, cell67, cell68, cell69, cell6A, cell6B, cell6C, cell6D, cell6E, cell6F,
                cell70, cell71, cell72, cell73, cell74, cell75, cell76, cell77, cell78, cell79, cell7A, cell7B, cell7C, cell7D, cell7E, cell7F,
                cell80, cell81, cell82, cell83, cell84, cell85, cell86, cell87, cell88, cell89, cell8A, cell8B, cell8C, cell8D, cell8E, cell8F,
                cell90, cell91, cell92, cell93, cell94, cell95, cell96, cell97, cell98, cell99, cell9A, cell9B, cell9C, cell9D, cell9E, cell9F,
                cellA0, cellA1, cellA2, cellA3, cellA4, cellA5, cellA6, cellA7, cellA8, cellA9, cellAA, cellAB, cellAC, cellAD, cellAE, cellAF,
                cellB0, cellB1, cellB2, cellB3, cellB4, cellB5, cellB6, cellB7, cellB8, cellB9, cellBA, cellBB, cellBC, cellBD, cellBE, cellBF,
                cellC0, cellC1, cellC2, cellC3, cellC4, cellC5, cellC6, cellC7, cellC8, cellC9, cellCA, cellCB, cellCC, cellCD, cellCE, cellCF,
                cellD0, cellD1, cellD2, cellD3, cellD4, cellD5, cellD6, cellD7, cellD8, cellD9, cellDA, cellDB, cellDC, cellDD, cellDE, cellDF,
                cellE0, cellE1, cellE2, cellE3, cellE4, cellE5, cellE6, cellE7, cellE8, cellE9, cellEA, cellEB, cellEC, cellED, cellEE, cellEF,
                cellF0, cellF1, cellF2, cellF3, cellF4, cellF5, cellF6, cellF7, cellF8, cellF9, cellFA, cellFB, cellFC, cellFD, cellFE, cellFF
        };
    }

    public void updateMemory() {
        for (int i = 0; i < this.memLabels.length; i++) {
            this.memLabels[i].setText(String.format("%02X", this.mc.getFile(i)));
        }
    }
}

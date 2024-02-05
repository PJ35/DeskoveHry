import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeskovkyFrame extends JFrame{
    private JPanel mainPanel;
    private JTextField name;
    private JCheckBox boughtCheckBox;
    private JRadioButton worstRadioButton;
    private JRadioButton middleRadioButton;
    private JRadioButton bestRadioButton;
    private JButton previousButton;
    private JButton nextButton;
    int index = 0;

    public DeskovkyFrame() {
        setContentPane(mainPanel);
        setTitle("Deskovky");
        setBounds(650, 350, 400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        if(Model.getSize() > 0) {
            zobrazDeskovku(index);
        }
        worstRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                middleRadioButton.setSelected(false);
                bestRadioButton.setSelected(false);
            }
        });
        middleRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                worstRadioButton.setSelected(false);
                bestRadioButton.setSelected(false);
            }
        });
        bestRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                worstRadioButton.setSelected(false);
                middleRadioButton.setSelected(false);
            }
        });
        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (index > 0) {
                    index--;
                    zobrazDeskovku(index);
                }
            }
        });
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (index < Model.getSize() - 1) {
                    index++;
                    zobrazDeskovku(index);
                }
            }
        });
    }

    public void zobrazDeskovku(int index) {
        Deskovka d = Model.getDeskovka(index);
        name.setText(d.getName());
        boughtCheckBox.setSelected(d.isBought());
        switch (d.getRating()) {
            case 1:
                worstRadioButton.setSelected(true);
                middleRadioButton.setSelected(false);
                bestRadioButton.setSelected(false);
                break;
            case 2:
                worstRadioButton.setSelected(false);
                middleRadioButton.setSelected(true);
                bestRadioButton.setSelected(false);
                break;
            case 3:
                worstRadioButton.setSelected(false);
                middleRadioButton.setSelected(false);
                bestRadioButton.setSelected(true);
                break;
        }
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

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
    String soubor = "deskovky.txt";

    public DeskovkyFrame() {
        loadFile(soubor);
        setContentPane(mainPanel);
        setTitle("Deskovky");
        setSize(400, 220);
        setToTheMiddle(this);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        if(Model.getSize() == 0) {
            addBlankPage();
        }
        zobrazDeskovku(index);
        iniMenu();
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
                } else {
                    int result = JOptionPane.showConfirmDialog(null,
                            "Dosáhli jste začátku seznamu.\nChcete se přesunout na konec seznamu?", "Přesunutí", JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.YES_OPTION) {
                        index = Model.getSize() - 1;
                        zobrazDeskovku(index);
                    }
                }
            }
        });
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (index < Model.getSize() - 1) {
                    index++;
                    zobrazDeskovku(index);
                } else {
                    int result = JOptionPane.showConfirmDialog(null,
                            "Dosáhli jste konce seznamu.\nChcete se přesunout na začátek seznamu?", "Přesunutí", JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.YES_OPTION) {
                        index = 0;
                        zobrazDeskovku(index);
                    }
                }
            }
        });
    }

    private void loadFile(String soubor) {
        String radek = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(soubor))) {
            while ((radek = reader.readLine()) != null) {
                String[] polozky = radek.split(";");
                Model.addDeskovka(new Deskovka(polozky[0], Boolean.parseBoolean(polozky[1]), Integer.parseInt(polozky[2])));
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Soubor "+soubor+" nenalezen!\n"+e.getMessage());
        } catch (IOException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, "Chyba při čtení ze souboru "+soubor+" na řádku "+radek+":\n"+e.getMessage());
        }
    }

    public void setToTheMiddle(JFrame frame) {
        GraphicsConfiguration gc = frame.getGraphicsConfiguration();
        Rectangle bounds = gc.getBounds();
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
        Rectangle effectiveScreenArea = new Rectangle();

        effectiveScreenArea.x = bounds.x + screenInsets.left;
        effectiveScreenArea.y = bounds.y + screenInsets.top;
        effectiveScreenArea.height = bounds.height - screenInsets.top - screenInsets.bottom;
        effectiveScreenArea.width = bounds.width - screenInsets.left - screenInsets.right;

        // Center:
        int middleX = effectiveScreenArea.x + (effectiveScreenArea.width - frame.getWidth()) / 2;
        int middleY = effectiveScreenArea.y + (effectiveScreenArea.height - frame.getHeight()) / 2;
        frame.setLocation(middleX, middleY);
    }

    private void iniMenu() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu menu = new JMenu("Soubor");
        menuBar.add(menu);
        JMenuItem menuItem = new JMenuItem("Uložit");
        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "Opravdu chcete uložit změny?", "Uložit",
                        JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    Model.setDeskovka(index, new Deskovka(name.getText(), boughtCheckBox.isSelected(), worstRadioButton.isSelected() ? 1
                            : middleRadioButton.isSelected() ? 2 : bestRadioButton.isSelected() ? 3 : 0));
                    updateFile(soubor);
                    JOptionPane.showMessageDialog(null, "Deskovka byla uložena.");
                }
            }
        });
        menuItem = new JMenuItem("Uložit jako");
        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Textové soubory", "txt"));
                int result = fc.showSaveDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    soubor = fc.getSelectedFile().getAbsolutePath();
                    soubor = soubor.endsWith(".txt") ? soubor : soubor + ".txt";
                    updateFile(soubor);
                    JOptionPane.showMessageDialog(null, "Byl vybrán soubor "+soubor);
                } else {
                    JOptionPane.showMessageDialog(null, "Nebyl vybrán žádný soubor.\nZůstává vybrán výchozí soubor "+soubor);
                }
            }
        });
        menuItem = new JMenuItem("Načíst");
        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Textové soubory", "txt"));
                int result = fc.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    soubor = fc.getSelectedFile().getAbsolutePath();
                    JOptionPane.showMessageDialog(null, "Byl vybrán soubor "+soubor);
                    loadFile(soubor);
                    index = 0;
                    zobrazDeskovku(index);
                } else {
                    JOptionPane.showMessageDialog(null, "Nebyl vybrán žádný soubor.\nZůstává vybrán výchozí soubor "+soubor);
                }
            }
        });
        menuItem = new JMenuItem("Obnovit");
        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "Opravdu chcete obnovit soubor?", "Obnovit",
                    JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    loadFile(soubor);
                    index = 0;
                    zobrazDeskovku(index);
                }
            }
        });
        menu = new JMenu("Akce");
        menuBar.add(menu);
        menuItem = new JMenuItem("Přidat");
        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Model.getSize() < Integer.MAX_VALUE) {
                    Model.addDeskovka(new Deskovka("Nová deskovka", false, 0));
                    index = Model.getSize() - 1;
                    zobrazDeskovku(index);
                    updateFile(soubor);
                } else {
                    JOptionPane.showMessageDialog(null, "Seznam je plný.");
                }
            }
        });
        menuItem = new JMenuItem("Odebrat");
        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "Opravdu chcete odebrat tuto deskovku?", "Odebrat",
                        JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    Model.removeDeskovka(index);
                    if (index > 0) {
                        index--;
                    }
                    if (Model.getSize() > 0) {
                        zobrazDeskovku(index);
                    } else {
                        addBlankPage();
                        zobrazDeskovku(index);
                    }
                    updateFile(soubor);
                }
            }
        });
        menuItem = new JMenuItem("Seřadit");
        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Model.sort();
                updateFile(soubor);
                JOptionPane.showMessageDialog(null, "Seznam byl seřazen.");
            }
        });
        menuItem = new JMenuItem("Obnovit");
        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "Opravdu chcete obnovit stránku?", "Obnovit",
                        JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    zobrazDeskovku(index);
                }
            }
        });
        menu = new JMenu("Souhrn");
        menuBar.add(menu);
        menuItem = new JMenuItem("Statistika");
        menu.add(menuItem);
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int celkem = Model.getSize();
                StringBuilder sb = new StringBuilder();
                int zakoupeno = 0;
                for (int i = 0; i < Model.getSize(); i++) {
                    if (Model.getDeskovka(i).getRating() == 3) {
                        sb.append("\n       ").append(Model.getDeskovka(i).getName());
                    }
                    if (Model.getDeskovka(i).isBought()) {
                        zakoupeno++;
                    }
                }
                JOptionPane.showMessageDialog(null, "Počet deskovek: "+celkem+"\nNejoblíbenější deskovky: "+sb+
                        "\nPočet zakoupených deskovek: "+zakoupeno);
            }
        });
    }

    private void addBlankPage() {
        Model.addDeskovka(new Deskovka("", false, 0));
    }

    public void zobrazDeskovku(int index) {
        Deskovka d = Model.getDeskovka(index);
        name.setText(d.getName());
        boughtCheckBox.setSelected(d.isBought());
        switch (d.getRating()) {
            case 1 -> {
                worstRadioButton.setSelected(true);
                middleRadioButton.setSelected(false);
                bestRadioButton.setSelected(false);
            }
            case 2 -> {
                worstRadioButton.setSelected(false);
                middleRadioButton.setSelected(true);
                bestRadioButton.setSelected(false);
            }
            case 3 -> {
                worstRadioButton.setSelected(false);
                middleRadioButton.setSelected(false);
                bestRadioButton.setSelected(true);
            }
            default -> {
                worstRadioButton.setSelected(false);
                middleRadioButton.setSelected(false);
                bestRadioButton.setSelected(false);
            }
        }
    }

    public void updateFile(String soubor) {
        String radek = "";
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(soubor)))) {
            for (int i = 0; i < Model.getSize(); i++) {
                Deskovka d = Model.getDeskovka(i);
                radek = d.getName() + ";" + d.isBought() + ";" + d.getRating();
                writer.println(radek);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Chyba při zápisu do souboru "+soubor+" na řádku "+radek+":\n"+e.getMessage());
        }
    }
}

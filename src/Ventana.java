import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.Locale;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Ventana extends JFrame implements ActionListener{
	// Declaracion de Componentes
	private JMenuBar barraMenu;									    // Barra para colocar elementos del menu principal
	private JMenu	[] Menus = new JMenu[3];						// menuArchivo, menuEdicion, menuAyuda --> Elementos del Menu Principal
	private JMenuItem [][] SubMenuI = new JMenuItem[3][5];			// Matr�z que contendr� los submen�s
	private static JTextArea cajaTexto;							    // Zona de escritura
	private UIManager.LookAndFeelInfo [] apariencias;
	public static void main(String [] args){
		new Ventana();
	}
	public Ventana() {
		super ( "MininiJava Compiler" );

		menu();
		cambioEstilo();
		String[] a = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

		GUILayer panel = new GUILayer();

		cajaTexto = panel.programa;
		cajaTexto.setFont(new Font(cajaTexto.getFont().getName(),cajaTexto.getFont().getStyle() ,12));

		add(panel);

		setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );
		setSize(1200, 800);
		setLocationRelativeTo(null);
		setIconImage(new ImageIcon("Imagenes/write.png").getImage());
		setVisible(true);
	}
	private void cambioEstilo()
	{
		apariencias = UIManager.getInstalledLookAndFeels();
		try
		{
			UIManager.setLookAndFeel( apariencias[ 3 ].getClassName() );
			SwingUtilities.updateComponentTreeUI( this );
		}
		catch ( Exception excepcion )
		{
			excepcion.printStackTrace();
		}

	}
	private void menu() {
																	                                                // Creacion de Componentes
		barraMenu 	= new JMenuBar();
		String [][] NombresMenu = {{"Archivo", "A"},
								   {"Edicion", "E"},
								   {"Ayuda"  , "y"}};
		String[][][] nombres =  {
									{
									  //Text			      Ruta		Mnemonic	ToolTip
									  {"Nuevo"		 , "newfile"	, "N", "Crear Nuevo Documento"				}, 	//	nuevo
									  {"Abrir"		 , "open"		, "b", "Abrir Documento"					},  //	abrir
									  {"Guardar Como", "save"		, "G", "Guardar Archivo Como..."			}, 	//	guardarComo
									  {"Imprimir"	 , "printer"	, "P", "Imprimir Archivo"					}, 	//  imprimir
									  {"Salir"		 					 , "Salir del Programa" 				}	//  salir
								  	}, 	// <-- Para men� Archivo
								  	{
									  {"Fuente..."	 , "fuente"		, "L", "Propiedades, tamano y tipo de letra"}, // tipoLetra
									  {"Cortar"		 , "cut"		, "X", "Ctrl + X"							}, // cortar
									  { "Copiar"	 , "copy"		, "C", "Ctrl + C"							}, // copiar
									  {"Pegar"		 , "paste"		, "V", "Ctrl + V"							}
									}, 	// <-- Para men� Edici�n
								  	{
									  {"Acerca De...", "about"		, "D", "Autores..."							}
									}  // <-- Para men� Ayuda
								};
		
		for(int i = 0; i< nombres.length; i++){						//Menus.length == nombres.length
			Menus[i] = new JMenu(NombresMenu[i][0]);
			Menus[i].setMnemonic(NombresMenu[i][1].charAt(0));
			for(int j = 0;j< nombres[i].length;j++){
					if( i == 0 && j == 4){							//Caso especial para salir(revisar nombres[][])
						SubMenuI[i][j] = new JMenuItem ( nombres[i][j][0] );
						SubMenuI[i][j].setToolTipText(nombres[i][j][1]);
					}else{
						SubMenuI[i][j] = new JMenuItem ( nombres[i][j][0], new ImageIcon("Imagenes/" +nombres[i][j][1]+".gif"));			//Aqu� da error Huehue seguro son los �nidices, o algo mal escrito
						SubMenuI[i][j].setMnemonic( nombres[i][j][2].charAt(0) );
						SubMenuI[i][j].setToolTipText(nombres[i][j][3]);
					}
					SubMenuI[i][j].addActionListener(this);
			}
		}
																	// Agregacion de Componentes al Menu
		for (int i = 0; i < SubMenuI.length; i++) {					// Menus.length == SubMenuI.length
			for (int j = 0; j < SubMenuI[i].length; j++)
				if(SubMenuI[i][j]!= null)
					Menus[i].add(SubMenuI[i][j]);
			barraMenu.add(Menus[i]);
		}

		setJMenuBar ( barraMenu );
	}

	public void actionPerformed(ActionEvent Evt) {
		if(Evt.getSource() instanceof JMenuItem){
			JMenuItem objeto = (JMenuItem) Evt.getSource();
			String text = objeto.getText();
			if(text.equals("Nuevo")){
				if(!cajaTexto.getText().equals("")){			// Si hay algo escrito, pregunta si desea borrarlo...
					int opcion = JOptionPane.showConfirmDialog( null, "Si crea un nuevo documento perdera toda la informacion capturada, \n"
											+ "Esta seguro de que quiere crear un nuevo documento?", "Mi Editor - Nuevo",
											JOptionPane.YES_NO_OPTION );
					if ( opcion == JOptionPane.YES_OPTION ) 
						cajaTexto.setText("");
				}
			}else if(text.equals("Imprimir")){
				try {
					cajaTexto.print();
				} catch (PrinterException e1) {
					e1.printStackTrace();
				}
			}else if(text.equals("Salir")){
				int opcion = JOptionPane.showConfirmDialog( null, "Esta seguro de que quiere Salir?",
						"Mi Editor - Salir", JOptionPane.YES_NO_OPTION );
				if ( opcion == JOptionPane.YES_OPTION ) 
					dispose();
			}else if(text.equals("Cortar")){
				cajaTexto.cut();
			}else if(text.equals("Copiar")){
				cajaTexto.copy();
			}else if(text.equals("Pegar")){
				cajaTexto.paste();
			}else if(text.equals("Acerca De...")){
				JDialog jDialog = new JDialog ( this , "Acerca de..." );
				jDialog.setSize( 500, 600 );
				jDialog.setVisible( true );
				
				JTextArea AcercaDe = new JTextArea ();
				AcercaDe.setText( "Tecnologico Nacional de Mexico\nInstituto Tecnologico de Culiacan"
								+ "\n\nIngenieria en Sistemas Computacionales"
								+ "\n\nLenguajes y Automatas II"
								+ "\nMininiJava"
								+ "\n\nEB01\n13:00 - 14:00"
								+ "\n\nDream Team:\nFigueroa valle Aldo Daniel \n" +
									"Gastelum Rodarte Juan José\n" +
									"López Malacón Miguel Ernesto\n" +
									"Ramírez de León Christian\n" +
									"Silva López Joel\n" +
									"Stone Beltrán Jorge Arturo\n" +
									"\n" );
				AcercaDe.setFont(new Font(AcercaDe.getFont().getName(),AcercaDe.getFont().getStyle() ,20));
				AcercaDe.setEditable( false );
				
				jDialog.add( AcercaDe );
				jDialog.update(jDialog.getGraphics());
			}else if(text.equals("Fuente...")){
				
				fuentes ( text );
			
			}else{
				JDialog jDialog = new JDialog(this,text);
				
				JFileChooser select = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");	//,"doc","odt","pdf","rtf","tex","wks","wps","wpd");
				select.addChoosableFileFilter(filter);
				
				select.setFileFilter( filter );
				select.addActionListener(this);
				
				jDialog.setSize(select.getPreferredSize().width, select.getPreferredSize().height);
				jDialog.setModal(true);
				jDialog.add(select);
				jDialog.setVisible(true);
			}
		}else if(Evt.getSource() instanceof JFileChooser){
			JFileChooser temp = (JFileChooser)Evt.getSource();
			JDialog frame = (JDialog)SwingUtilities.getWindowAncestor(temp);
			String command = Evt.getActionCommand();
			
			if(frame.getTitle().equals("Abrir")){
				if(command.equals(JFileChooser.APPROVE_SELECTION)){
					File archivo = temp.getSelectedFile();
					FileInputStream fis = null;

					try {
						fis = new FileInputStream(archivo);
						int content;
						String cadena = "";
						while ((content = fis.read()) != -1) {
							// convert to char and display it
							cadena += (char) content;
						}
						cajaTexto.setText(cadena);
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							if (fis != null)
								fis.close();
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
					frame.dispose();
				}else if(command.equals(JFileChooser.CANCEL_SELECTION))
					frame.dispose();
			}else if(frame.getTitle().equals("Guardar Como")){
				if(command.equals(JFileChooser.APPROVE_SELECTION)){
					FileOutputStream fop = null;
					String ruta = temp.getSelectedFile().getAbsolutePath();
					File archivo;
					String content = cajaTexto.getText();
					try {
						archivo = new File(ruta+((ruta.contains(".txt")?"":".txt")));

						if(!archivo.exists()){												// Si ya existe archivo con esa ruta escrito, pregunta si desea sobreescribirlo
							// if file doesnt exists, then create it
							archivo.createNewFile();
						}else {
							int opcion = JOptionPane.showConfirmDialog( null, "Ya existe este archivo, desea sobreescribirlo?",
									"Mi Editor - Robreescribir", JOptionPane.YES_NO_OPTION );
							if ( opcion == JOptionPane.NO_OPTION )
								return;
						}
						fop = new FileOutputStream(archivo);
						// get the content in bytes
						byte[] contentInBytes = content.getBytes();

						fop.write(contentInBytes);
						fop.flush();
						fop.close();

						System.out.println("Done");
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							if (fop != null) {
								fop.close();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					frame.dispose();
				}else if(command.equals(JFileChooser.CANCEL_SELECTION))
					frame.dispose();
			}
		}
	}
	
	public void fuentes ( String text ) { 
		JDialog jDialog = new JDialog ( this, text );
		jDialog.setSize ( 300, 250 );
		
		JPanel panelPrincipal = new JPanel();
		JPanel panelBotones   = new JPanel(new GridLayout(2,2,5,5));
		
		JList<String> listaFuentes = new JList<String>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames(Locale.US));
		
		listaFuentes.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		listaFuentes.setFocusable(true);
		listaFuentes.setSelectionBackground(new Color(125,255,0));
		
		JScrollPane scroll 		  = new JScrollPane(listaFuentes, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		JButton  	botonAceptar  = new JButton("Aceptar");
		JButton  	botonCancelar = new JButton("Cancelar");
		
		JSpinner tamanoLetra   = new JSpinner(new SpinnerNumberModel(16, 8, 72, 1));
		tamanoLetra.setValue(cajaTexto.getFont().getSize());

		String[] tL = {"Regular","Bold","Italic","Bold-Italic"};
		JComboBox tipoLetra    = new JComboBox(tL);
		tipoLetra.setSelectedIndex(cajaTexto.getFont().getStyle());
		
		botonAceptar.addActionListener(new ActionListener(){
			public void actionPerformed ( ActionEvent e ){
				listaFuentes.setSelectedValue(cajaTexto.getFont(), true);
				cajaTexto.setFont(new Font(listaFuentes.getSelectedValue(), tipoLetra.getSelectedIndex(), (int) tamanoLetra.getValue()));
				jDialog.dispose();
			}
		});
		botonCancelar.addActionListener(new ActionListener(){
			public void actionPerformed ( ActionEvent e ){
				jDialog.dispose();
			}
		});

		panelPrincipal.add( scroll );
		
		panelBotones.add(tipoLetra);
		panelBotones.add(tamanoLetra);
		panelBotones.add(botonAceptar);
		panelBotones.add(botonCancelar);
		
		jDialog.add( panelPrincipal );
		jDialog.add( panelBotones, BorderLayout.SOUTH );
		jDialog.setModal( true );
		jDialog.setResizable( false );
		jDialog.setVisible( true );
	}

}

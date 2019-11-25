package server;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import data.Data;

public class Server extends JFrame {
	
    private JButton btnStart;
    private JButton btnOpenFolder;
    private JButton btnChooseFile;
    private JButton btnSend;
    private JButton btnSend_1;
    private JLabel jLabel1;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JList<String> list;
    private JTextArea txt;
    private JTextField txtName;
    private File chosenFile;
    private double sizeSplitFile = 524288000;  // 500MB
    private String splitFileFolder = "E:\\LAP_TRINH_MANG\\splitFileData\\"; // folder luu file cat
    private String joinFileFolder = "E:\\LAP_TRINH_MANG\\serverData\\"; //folder luu file da nhan
    public Server() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel1 = new JLabel();
        btnStart = new JButton();
        jScrollPane1 = new JScrollPane();
        txt = new JTextArea();
        jScrollPane2 = new JScrollPane();
        list = new JList<>();
        btnOpenFolder = new JButton();
        btnChooseFile = new JButton();
        btnSend = new JButton();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setText("SERVER");

        btnStart.setText("START");
        btnStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnStartActionPerformed(evt);
            }
        });

        txt.setEditable(false);
        txt.setColumns(20);
        txt.setRows(5);
        jScrollPane1.setViewportView(txt);
        jScrollPane2.setViewportView(list);

        btnOpenFolder.setText("Open Folder");
        btnOpenFolder.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnOpenFolderActionPerformed(evt);
            }
        });
        
        JLabel lblNewLabel = new JLabel("List received");
        
        btnChooseFile.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent evt) {
        		btnChooseFileActionPerformed(evt);
        	}
			
        });
        btnChooseFile.setText("Choose file");
        
        txtName = new JTextField();
        
        btnSend.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent evt) {
        		btnSendActionPerformed(evt);
        	}
        });
        btnSend.setText("Send");

        GroupLayout layout = new GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(layout.createSequentialGroup()
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(layout.createSequentialGroup()
        					.addGap(19)
        					.addComponent(jLabel1)
        					.addPreferredGap(ComponentPlacement.RELATED, 365, Short.MAX_VALUE)
        					.addComponent(btnStart))
        				.addGroup(layout.createSequentialGroup()
        					.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 346, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(jScrollPane2, 0, 0, Short.MAX_VALUE)))
        			.addContainerGap())
        		.addGroup(layout.createSequentialGroup()
            			.addContainerGap(28, Short.MAX_VALUE)
            			.addComponent(btnChooseFile, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
            			.addPreferredGap(ComponentPlacement.UNRELATED)
            			.addComponent(txtName, GroupLayout.PREFERRED_SIZE, 306, GroupLayout.PREFERRED_SIZE)
            			.addPreferredGap(ComponentPlacement.UNRELATED)
            			.addComponent(btnSend, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
            			.addGap(31))
        		.addGroup(layout.createSequentialGroup()
        			.addGap(403)
        			.addComponent(btnOpenFolder, GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
        			.addGap(50))        	
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap(374, Short.MAX_VALUE)
        			.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE)
        			.addGap(38))
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(btnStart)
        				.addComponent(jLabel1))
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(btnChooseFile)
        				.addComponent(txtName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(btnSend))
        			.addComponent(lblNewLabel)
        			.addGap(7)
        			.addGroup(layout.createParallelGroup(Alignment.TRAILING, false)
        				.addComponent(jScrollPane2)
        				.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE))
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(btnOpenFolder)
        			.addGap(29)
        			.addContainerGap(32, Short.MAX_VALUE))
        );
        getContentPane().setLayout(layout);
        
        pack();
        setLocationRelativeTo(null);
    }
    private ServerSocket server;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private DefaultListModel mod = new DefaultListModel();
    
    private void btnStartActionPerformed(ActionEvent evt) {
        list.setModel(mod);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server = new ServerSocket(9999);
                    txt.append("Server started ...\n");
                    Socket s = server.accept();
                    in = new ObjectInputStream(s.getInputStream());
                    out = new ObjectOutputStream(s.getOutputStream());
                    Data data = (Data) in.readObject();
                    String name = data.getName();
                    txt.append("New client " + name + " has been connected!\n");
                    while (true) {
                        data = (Data) in.readObject();
                        // lưu file (cả file lớn và file bình thường)
                        FileOutputStream fos = new FileOutputStream(joinFileFolder + data.getName());
                        fos.write(data.getFile());
                        fos.close();
                        // nhận file thường (<500 MB)
                        if ("normalFile".equals(data.getStatus())) {
                        	data.setName(data.getName());
                        	mod.addElement(data);
                        	txt.append("Đã nhận file thành công! \n");
                        }
                        if ("done".equals(data.getStatus())) {
                        	txt.append("Đã nhận các file thành công! \n");
                        	txt.append("Đang nối file... \n");
                        	joinFile(joinFileFolder + data.getName());
                        	data.setName(data.getName().substring(0, data.getName().lastIndexOf('.')));
                        	//data.setName(data.getName());
                        	mod.addElement(data);
                        	txt.append("Xong! \n");
                        }
                    } 
                    
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(Server.this, e, "Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        }).start();
    }
    
    private boolean joinFile(String source) throws FileNotFoundException, IOException {
      String sourceFile = source.substring(0, source.lastIndexOf('.'));
      File file = new File(sourceFile);
      OutputStream os = new FileOutputStream(file);
      InputStream is;
      int count = 1; // duoi so thu tu file
      while (true) {
          String path = file + "." + count; // cac phan file da cat
          File eachFile = new File(path);
          if (eachFile.exists()) {
              is = new FileInputStream(eachFile);
              int i = 0;
              byte[] arr = new byte[8*1024]; // doc file va ghi file theo tung doan arr
              while ((i = is.read(arr)) != -1) {
                  os.write(arr, 0, i);
              }
              os.flush();
              is.close();
              count++;
              // xóa phần file này
              eachFile.delete();
              
          } else {
              break;
          }
      }
      os.close();
      return false;
    }

    private void btnOpenFolderActionPerformed(ActionEvent evt) {
    	JFileChooser ch = new JFileChooser(joinFileFolder);
    	int c = ch.showOpenDialog(this);
    }

    // btnChooseFile action
    private void btnChooseFileActionPerformed(ActionEvent evt) {
    	try {
            JFileChooser ch = new JFileChooser();
            int c = ch.showOpenDialog(this);
            if (c == JFileChooser.APPROVE_OPTION) {
            	chosenFile = ch.getSelectedFile();
                txtName.setText(chosenFile.getName());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
	}
    
    // btnSend action
	private void btnSendActionPerformed(ActionEvent evt) {
		try {
			if (chosenFile != null) {
    			txt.append("Pending...\n");
    			// cắt và gửi file
        		splitFile(chosenFile.getPath());
        		txtName.setText("");
        		txt.append("Send file successfully!\n");
                chosenFile = null;
                /*File dir = new File(splitFileFolder);
                File[] listFile = dir.listFiles();
                for (File file : listFile) {
    				file.delete();
    			}*/
    		} else {
    			JOptionPane.showMessageDialog(this, "Bạn chưa chọn file!", "Error", JOptionPane.ERROR_MESSAGE);
    		}
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Bạn chưa chọn file!", "Error", JOptionPane.ERROR_MESSAGE);
        }
	}
	
	// split file
    private void splitFile(String source) throws FileNotFoundException, IOException {
    	int count = 0;
    	byte b[] = new byte[(int) sizeSplitFile];
    	File sourceFile = new File(source);
        if (sourceFile.exists() && sourceFile.isFile()) {
            long sizeFile = sourceFile.length();
            // nếu file > 500 Mb thì cắt rồi send, nếu không thì send luôn.
            if (sizeFile > sizeSplitFile) {
            	String status = "largeFile";
            	int numberFile = (int) Math.ceil(sizeFile/sizeSplitFile); // tính số file sẽ cắt được
                System.out.println("Số file sẽ cắt: " + numberFile);
                InputStream is = new FileInputStream(sourceFile);
                byte[] arr = new byte[8*1024];
                for (int i = 1; i <= numberFile; i++) {
                    int j = 0;
                    long a = 0;
                    String splitFilePath = splitFileFolder + sourceFile.getName() + "." + i;
                    OutputStream os = new FileOutputStream(splitFilePath);
                    System.out.println(sourceFile.getName() + "." + i);
                    while ((j = is.read(arr)) != -1) {
                        os.write(arr, 0, j);
                        a += j;
                        if (a >= sizeSplitFile) {
                            break;
                        }
                    }
                    count++; // đếm số file đã gửi
                    sendData(splitFilePath, numberFile, count, b, status);
                    txt.setText("Đã gửi " + count + "/" + numberFile + "\n");
                    os.flush();
                    os.close();
                }
                is.close();
                
            } else {
            	String status = "normalFile";
            	sendData(source, 1, 1, b, status);
            }
            
        } else {
            System.out.println("File không tồn tại");
        }
    }
    
    private void sendData (String filePath, int numberFile, int count, byte[] b, String status) {
    	try {
			File file = new File(filePath);
			FileInputStream in = new FileInputStream(file);
			if (count == numberFile) {
				b = new byte[in.available()];
			}
            in.read(b);
            Data data = new Data();
            if ("normalFile".equals(status)) {
            	data.setStatus(status);
            } else {
            	if (count == numberFile) {
                	data.setStatus("done");
                } else {
                	data.setStatus(status);
                }
            }
            
            data.setName(file.getName());
            data.setFile(b);
            out.writeObject(data);
            out.flush();
            
				
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Something was wrong!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public static void main(String args[]) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

        /* display the form */
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Server().setVisible(true);
            }
        });
    }
}

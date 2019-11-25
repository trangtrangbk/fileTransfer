package client;

import data.Data;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class Client extends JFrame {
    private JButton btnConnect;
    private JButton btnChooseFile;
    private JButton btnSendFile ;
    private JButton btnOpenFolder;
    private JLabel label1;
    private JLabel label2;
    private JScrollPane jScrollPane1;
    private JTextArea txt;
    private JTextField txtIp;
    private JTextField txtName;
    private File chosenFile; 
    private double sizeSplitFile = 524288000;  // 500MB
    private String splitFileFolder = "E:\\LAP_TRINH_MANG\\splitFileData\\"; // folder luu cac file cat tu file lon
    private String joinFileFolder = "E:\\LAP_TRINH_MANG\\serverData\\"; // folder luu cac file sau khi ghep
    
    public Client() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        label1 = new JLabel();
        btnConnect = new JButton();
        jScrollPane1 = new JScrollPane();
        txt = new JTextArea();
        btnChooseFile = new JButton();
        txtName = new JTextField();
        txtIp = new JTextField();
        label2 = new JLabel();
        btnSendFile = new JButton();
        btnOpenFolder = new JButton();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // thiet lap giao dien
        label1.setFont(new Font("Arial", 1, 18)); 
        label1.setText("CLIENT");

        btnConnect.setText("CONNECT");
        btnConnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnConnectActionPerformed(evt);
            }
        });

        txt.setEditable(false);
        txt.setColumns(20);
        txt.setRows(5);
        jScrollPane1.setViewportView(txt);

        btnChooseFile.setText("Choose file");
        btnChooseFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnChooseFileActionPerformed(evt);
            }
        });

        label2.setText("IP");
        
        btnSendFile.setText("Send");
        btnSendFile.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent evt) {
        		btnSendFileActionPerformed(evt);
        	}
        });
        
        scrollPane = new JScrollPane();
        
        label = new JLabel("List received");
        
        btnOpenFolder.setText("Open Folder");
        btnOpenFolder.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent evt) {
        		btnOpenFolderActionPerformed(evt);
        	}
        });
        
        GroupLayout layout = new GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(layout.createSequentialGroup()
        			.addGap(21)
        			.addComponent(label1)
        			.addPreferredGap(ComponentPlacement.RELATED, 263, Short.MAX_VALUE)
        			.addComponent(label2, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
        			.addGap(18)
        			.addComponent(txtIp, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE)
        			.addGap(18)
        			.addComponent(btnConnect)
        			.addGap(18))
        		.addGroup(layout.createSequentialGroup()
            			.addGroup(layout.createParallelGroup(Alignment.TRAILING)
            				.addGroup(layout.createSequentialGroup()
            					.addContainerGap()
            					.addComponent(btnOpenFolder, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE))
            				.addGroup(layout.createSequentialGroup()
            					.addGap(62)
            					.addComponent(btnChooseFile, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
            					.addGap(14)
            					.addComponent(txtName, GroupLayout.PREFERRED_SIZE, 350, GroupLayout.PREFERRED_SIZE)
            					.addPreferredGap(ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
            					.addComponent(btnSendFile, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)))
            			.addGap(57))
        		.addGroup(Alignment.LEADING, layout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 383, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
        			.addContainerGap())
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap(430, Short.MAX_VALUE)
        			.addComponent(label, GroupLayout.PREFERRED_SIZE, 133, GroupLayout.PREFERRED_SIZE)
        			.addGap(58))
        		
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(btnConnect)
        				.addComponent(txtIp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(label2)
        				.addComponent(label1))
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addGroup(layout.createParallelGroup(Alignment.BASELINE, false)
            				.addComponent(txtName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
            				.addComponent(btnChooseFile)
            				.addComponent(btnSendFile))
            			.addGap(20)
        			.addComponent(label)
        			.addGap(2)
        			.addGroup(layout.createParallelGroup(Alignment.TRAILING, false)
        				.addComponent(scrollPane)
        				.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(btnOpenFolder)
        			.addGap(19))
        );
        
        list = new JList<String>();
        scrollPane.setViewportView(list);
        getContentPane().setLayout(layout);

        pack();
        setLocationRelativeTo(null);
    }

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private DefaultListModel mod = new DefaultListModel();
    private JScrollPane scrollPane;
    private JList<String> list;
    private JLabel label;
    
    // xu li event CONNECT
    private void btnConnectActionPerformed(ActionEvent evt) {
    	list.setModel(mod);
    	new Thread(new Runnable() {
        	@Override
			public void run() {
        		try {
                    socket = new Socket(txtIp.getText().trim(), 9999);
                    txt.append("Connected\n");
                    out = new ObjectOutputStream(socket.getOutputStream());
                    
                    Data client = new Data(); // tao doi tuong client 
                    client.setStatus("new");
                    client.setName("Trang Tran");
                    out.writeObject(client);
                    out.flush();
                    
                    in = new ObjectInputStream(socket.getInputStream());
                    Data data;
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
                        	txt.append("Received file successfully! \n");
                        }
                        if ("done".equals(data.getStatus())) {
                        	txt.append("Join file... \n");
                        	joinFile(joinFileFolder + data.getName());
                        	data.setName(data.getName().substring(0, data.getName().lastIndexOf('.')));
                        	//data.setName(data.getName());
                        	mod.addElement(data);
                        	txt.append("Received file successfully! \n");
                        }
        			}
                } catch (Exception e) {
                	//show message loi ket noi
                    JOptionPane.showMessageDialog(Client.this, "Connection failed!", "Error", JOptionPane.ERROR_MESSAGE);
                }
			}
		}).start();
    	
    }
    //xu li noi file
    private boolean joinFile(String source) throws FileNotFoundException, IOException {
        String sourceFile = source.substring(0, source.lastIndexOf('.')); 
        File file = new File(sourceFile);
        OutputStream os = new FileOutputStream(file);
        InputStream is;
        int count = 1; // duoi so thu tu file
        while (true) {
            String path = file + "." + count;  // cac phan file da cat
            File eachFile = new File(path);
            if (eachFile.exists()) {
                is = new FileInputStream(eachFile);
                int i = 0;
                byte[] arr = new byte[8*1024]; //doc file va ghi file theo tung doan arr
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
    // xu ly event chon file
    private void btnChooseFileActionPerformed(ActionEvent evt) {
        try {
            JFileChooser ch = new JFileChooser();
            int c = ch.showOpenDialog(this); // show folder 
            if (c == JFileChooser.APPROVE_OPTION) {
            	chosenFile = ch.getSelectedFile();
                txtName.setText(chosenFile.getName()); // hien thi ten file trong text field
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    //xu li event send file
    private void btnSendFileActionPerformed(ActionEvent evt) {
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
    			JOptionPane.showMessageDialog(this, "No file has chosen!", "Error", JOptionPane.ERROR_MESSAGE);
    		}
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra!", "Error", JOptionPane.ERROR_MESSAGE);
        }
	}
    
    // cut file
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
    
    // xem thư mục lưu file đã nhận
    private void btnOpenFolderActionPerformed(ActionEvent evt) {
    	JFileChooser ch = new JFileChooser(joinFileFolder);
    	int c = ch.showOpenDialog(this);
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
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Client().setVisible(true);
            }
        });
    }
}

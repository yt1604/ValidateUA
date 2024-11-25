package burp;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class BurpExtender
        extends AbstractTableModel
        implements IBurpExtender, ITab, IHttpListener, IScannerCheck, IMessageEditorController {
    private IBurpExtenderCallbacks callbacks;
    private IExtensionHelpers helpers;
    private JSplitPane splitPane;
    private IMessageEditor requestViewer;
    private IMessageEditor responseViewer;
    private IMessageEditor requestViewer_1;
    private IMessageEditor responseViewer_1;
    private IMessageEditor requestViewer_2;
    private IMessageEditor responseViewer_2;
    /*  44 */
    private final List < LogEntry > log = new ArrayList < > ();
    private IHttpRequestResponse currentlyDisplayedItem;
    private IHttpRequestResponse currentlyDisplayedItem_1;
    private IHttpRequestResponse currentlyDisplayedItem_2;
    /*  48 */
    private final List < Request_md5 > log4_md5 = new ArrayList < > ();
    public PrintWriter stdout;
    JTabbedPane tabs;
    /*  51 */
    int switchs = 0;
    /*  52 */
    int conut = 0;
    int original_data_len;
    String temp_data;
    /*  55 */
    int select_row = 0;
    Table logTable;
    /*  57 */
    String white_URL = "";
    /*  58 */
    int white_switchs = 0;
    /*  59 */
    String data_1 = "";
    /*  60 */
    String data_2 = "";
    /*  61 */
    String universal_cookie = "";

    String xy_version = "1.0";

    boolean lowPrivilegeSelected = false;

    boolean unauthorizedSelected = false;

    public void registerExtenderCallbacks(final IBurpExtenderCallbacks callbacks) {
        /*  68 */
        this.stdout = new PrintWriter(callbacks.getStdout(), true);
        /*  69 */
        this.stdout.println("hello ValidateUA!");
        /*  70 */
        this.stdout.println("你好 欢迎使用 ValidateUA!");
        /*  71 */
        this.stdout.println("version:" + this.xy_version);
        /*  76 */
        this.callbacks = callbacks;
        /*  79 */
        this.helpers = callbacks.getHelpers();
        /*  82 */
        callbacks.setExtensionName("ValidateUA V" + this.xy_version);
        /*  85 */
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                /*  92 */
                BurpExtender.this.splitPane = new JSplitPane(1);
                /*  93 */
                JSplitPane splitPanes = new JSplitPane(0);
                /*  94 */
                JSplitPane splitPanes_2 = new JSplitPane(0);
                /*  97 */
                BurpExtender.this.logTable = new BurpExtender.Table(BurpExtender.this);
                /*  98 */
                BurpExtender.this.logTable.getColumnModel().getColumn(0).setPreferredWidth(10);
                /*  99 */
                BurpExtender.this.logTable.getColumnModel().getColumn(1).setPreferredWidth(50);
                /* 100 */
                BurpExtender.this.logTable.getColumnModel().getColumn(2).setPreferredWidth(300);
                /* 101 */
                JScrollPane scrollPane = new JScrollPane(BurpExtender.this.logTable);
                /* 104 */
                JPanel jp = new JPanel();
                /* 105 */
                jp.setLayout(new GridLayout(1, 1));
                /* 106 */
                jp.add(scrollPane);
                /* 109 */
                JPanel jps = new JPanel();
                /* 110 */
                jps.setLayout(new GridLayout(12, 1));
                /* 111 */
                JLabel jls = new JLabel("插件名：越权");
                /* 112 */
                JLabel jls_1 = new JLabel("用于扫描未授权、越权");
                /* 113 */
                JLabel jls_2 = new JLabel("版本：ValidateUA V" + BurpExtender.this.xy_version);
                /* 114 */
                JLabel jls_3 = new JLabel("感谢名单：");
                /* 115 */
                final JCheckBox chkbox1 = new JCheckBox("启动插件");

                /**
                 * */
                final JCheckBox lowPrivilege = new JCheckBox("低权限");

                final JCheckBox unauthorized = new JCheckBox("未授权");

                lowPrivilege.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        BurpExtender.this.lowPrivilegeSelected = lowPrivilege.isSelected();
                    }
                });

                unauthorized.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        BurpExtender.this.unauthorizedSelected = unauthorized.isSelected();
                    }
                });


                /* 116 */
                final JCheckBox chkbox2 = new JCheckBox("启动万能cookie");
                /* 117 */
                JLabel jls_5 = new JLabel("如果需要多个域名加白请用,隔开");
                /* 118 */
                final JTextField textField = new JTextField("填写白名单域名");
                /* 122 */
                JButton btn1 = new JButton("清空列表");
                /* 123 */
                final JButton btn3 = new JButton("启动白名单");
                /* 128 */
                JPanel jps_2 = new JPanel();
                /* 129 */
                JLabel jps_2_jls_1 = new JLabel("越权：填写低权限认证信息,将会替换或新增");
                /* 130 */
                final JTextArea jta = new JTextArea("Cookie: JSESSIONID=test;UUID=1; userid=admin\nAuthorization: Bearer test", 5, 30);
                /* 132 */
                JScrollPane jsp = new JScrollPane(jta);
                /* 135 */
                JLabel jps_2_jls_2 = new JLabel("未授权：将移除下列头部认证信息,区分大小写");
                /* 136 */
                final JTextArea jta_1 = new JTextArea("Cookie\nAuthorization\nToken", 5, 30);
                /* 138 */
                JScrollPane jsp_1 = new JScrollPane(jta_1);
                /* 141 */
                jps_2.add(jps_2_jls_1);
                /* 142 */
                jps_2.add(jsp);
                /* 143 */
                jps_2.add(jps_2_jls_2);
                /* 144 */
                jps_2.add(jsp_1);
                /* 147 */
                jps_2.setLayout(new GridLayout(5, 1, 0, 0));
                /* 152 */
                chkbox1.addItemListener(new ItemListener() {
                    public void itemStateChanged(ItemEvent e) {
                        /* 155 */
                        if(chkbox1.isSelected()) {
                            /* 156 */
                            BurpExtender.this.switchs = 1;
                            /* 158 */
                            BurpExtender.this.data_1 = jta.getText();
                            /* 159 */
                            BurpExtender.this.data_2 = jta_1.getText();
                            /* 161 */
                            jta.setForeground(Color.BLACK);
                            /* 162 */
                            jta.setBackground(Color.LIGHT_GRAY);
                            /* 163 */
                            jta.setEditable(false);
                            /* 165 */
                            jta_1.setForeground(Color.BLACK);
                            /* 166 */
                            jta_1.setBackground(Color.LIGHT_GRAY);
                            /* 167 */
                            jta_1.setEditable(false);
                        } else {
                            /* 169 */
                            BurpExtender.this.switchs = 0;
                            /* 171 */
                            jta.setForeground(Color.BLACK);
                            /* 172 */
                            jta.setBackground(Color.WHITE);
                            /* 173 */
                            jta.setEditable(true);
                            /* 175 */
                            jta_1.setForeground(Color.BLACK);
                            /* 176 */
                            jta_1.setBackground(Color.WHITE);
                            /* 177 */
                            jta_1.setEditable(true);
                        }
                    }
                });




                /* 183 */
                chkbox2.addItemListener(new ItemListener() {
                    public void itemStateChanged(ItemEvent e) {
                        /* 186 */
                        if(chkbox2.isSelected()) {
                            /* 187 */
                            BurpExtender.this.universal_cookie = "";
                        } else {
                            /* 189 */
                            BurpExtender.this.universal_cookie = "";
                        }
                    }
                });
                /* 195 */
                btn1.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        /* 198 */
                        BurpExtender.this.log.clear();
                        /* 199 */
                        BurpExtender.this.conut = 0;
                        /* 200 */
                        BurpExtender.this.log4_md5.clear();
                        /* 201 */
                        BurpExtender.this.fireTableRowsInserted(BurpExtender.this.log.size(), BurpExtender.this.log.size());
                    }
                });
                /* 204 */
                btn3.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        /* 207 */
                        if(btn3.getText().equals("启动白名单")) {
                            /* 208 */
                            btn3.setText("关闭白名单");
                            /* 209 */
                            BurpExtender.this.white_URL = textField.getText();
                            /* 210 */
                            BurpExtender.this.white_switchs = 1;
                            /* 211 */
                            textField.setEditable(false);
                            /* 212 */
                            textField.setForeground(Color.GRAY);
                        } else {
                            /* 214 */
                            btn3.setText("启动白名单");
                            /* 215 */
                            BurpExtender.this.white_switchs = 0;
                            /* 216 */
                            textField.setEditable(true);
                            /* 217 */
                            textField.setForeground(Color.BLACK);
                        }
                    }
                });
                /* 238 */
                jps.add(jls);
                /* 239 */
                jps.add(jls_1);
                /* 240 */
                jps.add(jls_2);
                /* 241 */
                jps.add(jls_3);
                /* 242 */
                jps.add(chkbox1);

                jps.add(lowPrivilege);

                jps.add(unauthorized);

                /* 244 */
                jps.add(btn1);
                /* 245 */
                jps.add(jls_5);
                /* 246 */
                jps.add(textField);
                /* 247 */
                jps.add(btn3);
                /* 264 */
                BurpExtender.this.tabs = new JTabbedPane();
                /* 265 */
                BurpExtender.this.requestViewer = callbacks.createMessageEditor(BurpExtender.this, false);
                /* 266 */
                BurpExtender.this.responseViewer = callbacks.createMessageEditor(BurpExtender.this, false);
                /* 267 */
                BurpExtender.this.requestViewer_1 = callbacks.createMessageEditor(BurpExtender.this, false);
                /* 268 */
                BurpExtender.this.responseViewer_1 = callbacks.createMessageEditor(BurpExtender.this, false);
                /* 269 */
                BurpExtender.this.requestViewer_2 = callbacks.createMessageEditor(BurpExtender.this, false);
                /* 270 */
                BurpExtender.this.responseViewer_2 = callbacks.createMessageEditor(BurpExtender.this, false);
                /* 272 */
                JSplitPane y_jp = new JSplitPane(1);
                /* 273 */
                y_jp.setDividerLocation(500);
                /* 274 */
                y_jp.setLeftComponent(BurpExtender.this.requestViewer.getComponent());
                /* 275 */
                y_jp.setRightComponent(BurpExtender.this.responseViewer.getComponent());
                /* 277 */
                JSplitPane d_jp = new JSplitPane(1);
                /* 278 */
                d_jp.setDividerLocation(500);
                /* 279 */
                d_jp.setLeftComponent(BurpExtender.this.requestViewer_1.getComponent());
                /* 280 */
                d_jp.setRightComponent(BurpExtender.this.responseViewer_1.getComponent());
                /* 282 */
                JSplitPane w_jp = new JSplitPane(1);
                /* 283 */
                w_jp.setDividerLocation(500);
                /* 284 */
                w_jp.setLeftComponent(BurpExtender.this.requestViewer_2.getComponent());
                /* 285 */
                w_jp.setRightComponent(BurpExtender.this.responseViewer_2.getComponent());
                /* 287 */
                BurpExtender.this.tabs.addTab("原始数据包", y_jp);
                /* 288 */
                BurpExtender.this.tabs.addTab("低权限数据包", d_jp);
                /* 289 */
                BurpExtender.this.tabs.addTab("未授权数据包", w_jp);
                /* 300 */
                splitPanes_2.setLeftComponent(jps);
                /* 301 */
                splitPanes_2.setRightComponent(jps_2);
                /* 304 */
                splitPanes.setLeftComponent(jp);
                /* 305 */
                splitPanes.setRightComponent(BurpExtender.this.tabs);
                /* 308 */
                BurpExtender.this.splitPane.setLeftComponent(splitPanes);
                /* 309 */
                BurpExtender.this.splitPane.setRightComponent(splitPanes_2);
                /* 310 */
                BurpExtender.this.splitPane.setDividerLocation(1000);
                /* 313 */
                callbacks.customizeUiComponent(BurpExtender.this.splitPane);
                /* 314 */
                callbacks.customizeUiComponent(BurpExtender.this.logTable);
                /* 315 */
                callbacks.customizeUiComponent(scrollPane);
                /* 316 */
                callbacks.customizeUiComponent(jps);
                /* 317 */
                callbacks.customizeUiComponent(jp);
                /* 318 */
                callbacks.customizeUiComponent(BurpExtender.this.tabs);
                /* 321 */
                callbacks.addSuiteTab(BurpExtender.this);
                /* 324 */
                callbacks.registerHttpListener(BurpExtender.this);
                /* 325 */
                callbacks.registerScannerCheck(BurpExtender.this);
            }
        });
    }
    public String getTabCaption() {
        /* 335 */
        return "ValidateUA";
    }
    public Component getUiComponent() {
        /* 341 */
        return this.splitPane;
    }
    public void processHttpMessage(final int toolFlag, boolean messageIsRequest, final IHttpRequestResponse messageInfo) {
        /* 349 */
        if(this.switchs == 1 &&
                /* 350 */
                toolFlag == 4) {
            /* 352 */
            if(!messageIsRequest) {
                /* 355 */
                synchronized(this.log) {
                    /* 358 */
                    Thread thread = new Thread(new Runnable() {
                        public void run() {
                            try {
                                /* 361 */
                                BurpExtender.this.checkVul(messageInfo, toolFlag);
                                /* 362 */
                            } catch (Exception ex) {
                                /* 363 */
                                ex.printStackTrace();
                                /* 364 */
                                BurpExtender.this.stdout.println(ex);
                            }
                        }
                    });
                    /* 368 */
                    thread.start();
                }
            }
        }
    }
    public List < IScanIssue > doPassiveScan(IHttpRequestResponse baseRequestResponse) {
        /* 379 */
        return null;
    }
    private void checkVul(IHttpRequestResponse baseRequestResponse, int toolFlag) {
        /* 383 */
        this.temp_data = String.valueOf(this.helpers.analyzeRequest(baseRequestResponse).getUrl());
        /* 384 */
        this.original_data_len = (baseRequestResponse.getResponse()).length;
        /* 385 */
        int original_len = this.original_data_len - this.helpers.analyzeResponse(baseRequestResponse.getResponse()).getBodyOffset();
        /* 386 */
        String[] temp_data_strarray = this.temp_data.split("\\?");
        /* 387 */
        String temp_data = temp_data_strarray[0];

        this.stdout.println(temp_data);

        /* 390 */
        String[] white_URL_list = this.white_URL.split(",");
        /* 391 */
        int white_swith = 0;
        /* 392 */
        if(this.white_switchs == 1) {
            /* 393 */
            white_swith = 0;
            /* 394 */
            for(int k = 0; k < white_URL_list.length; k++) {
                /* 395 */
                if(temp_data.contains(white_URL_list[k])) {
                    /* 396 */
                    this.stdout.println("白名单URL！" + temp_data);
                    /* 397 */
                    white_swith = 1;
                }
            }
            /* 400 */
            if(white_swith == 0) {
                /* 401 */
                this.stdout.println("不是白名单URL！" + temp_data);
                return;
            }
        }
        /* 407 */
        if(toolFlag == 4 || toolFlag == 64) {
            /* 408 */
            String[] static_file = {
                    "jpg",
                    "png",
                    "gif",
                    "css",
                    "js",
                    "pdf",
                    "mp3",
                    "mp4",
                    "avi",
                    "map",
                    "svg",
                    "ico",
                    "svg",
                    "woff",
                    "woff2",
                    "ttf"
            };
            /* 409 */
            String[] static_file_1 = temp_data.split("\\.");
            /* 410 */
            String static_file_2 = static_file_1[static_file_1.length - 1];
            /* 411 */
            for(String str: static_file) {
                /* 412 */
                if(static_file_2.equals(str)) {
                    /* 413 */
                    this.stdout.println("当前url为静态文件：" + temp_data + "\n");
                    return;
                }
            }
        }
        /* 421 */
        List < IParameter > paraLists = this.helpers.analyzeRequest(baseRequestResponse).getParameters();
        /* 422 */
        for(IParameter para: paraLists) {
            /* 423 */
            temp_data = temp_data + "+" + para.getName();
        }
        /* 427 */
        /* 用于校验是否为重复请求 */
        temp_data = temp_data + "+" + this.helpers.analyzeRequest(baseRequestResponse).getMethod();
        /* 428 */
        this.stdout.println("\nMD5(\"" + temp_data + "\")");
        /* 429 */
        temp_data = MD5(temp_data);
        /* 430 */
        this.stdout.println(temp_data);
        /* 432 */
        for(Request_md5 request_md5: this.log4_md5) {
            /* 433 */
            if(request_md5.md5_data.equals(temp_data)) {
                return;
            }
        }
        /* 437 */
        this.log4_md5.add(new Request_md5(temp_data));


        /* 442 */
        IRequestInfo analyIRequestInfo = this.helpers.analyzeRequest(baseRequestResponse);
        /* 443 */
        IHttpService iHttpService = baseRequestResponse.getHttpService();
        /* 444 */
        String request = this.helpers.bytesToString(baseRequestResponse.getRequest());
        /* 445 */
        int bodyOffset = analyIRequestInfo.getBodyOffset();
        /* 446 */
        byte[] body = request.substring(bodyOffset).getBytes();

        String low_len_data = "";

        /* 低权限 */
        if(BurpExtender.this.lowPrivilegeSelected){

            /* 450 */
            List < String > headers_y = analyIRequestInfo.getHeaders();
            /* 452 */
            String[] data_1_list = this.data_1.split("\n");
            int i;
            /* 453 */
            for(i = 0; i < headers_y.size(); i++) {
                /* 454 */
                String head_key = ((String) headers_y.get(i)).split(":")[0];
                /* 455 */
                for(int y = 0; y < data_1_list.length; y++) {
                    /* 456 */
                    if(head_key.equals(data_1_list[y].split(":")[0])) {
                        /* 457 */
                        headers_y.remove(i);
                        /* 458 */
                        i--;
                    }
                }
            }
            /* 464 */
            for(i = 0; i < data_1_list.length; i++) {
                /* 465 */
                headers_y.add(headers_y.size() / 2, data_1_list[i]);
            }
            /* 469 */
            byte[] newRequest_y = this.helpers.buildHttpMessage(headers_y, body);
            /* 470 */
            IHttpRequestResponse requestResponse_y = this.callbacks.makeHttpRequest(iHttpService, newRequest_y);
            /* 471 */
            int low_len = (requestResponse_y.getResponse()).length - this.helpers.analyzeResponse(requestResponse_y.getResponse()).getBodyOffset();
            /* 472 */

            /* 473 */
            if(original_len == 0) {
                /* 474 */
                low_len_data = Integer.toString(low_len);
                /* 475 */
            } else if(original_len == low_len) {
                /* 476 */
                low_len_data = Integer.toString(low_len) + "  ✔";
            } else {
                /* 478 */
                low_len_data = Integer.toString(low_len) + "  ==> " + Integer.toString(original_len - low_len);
            }

        }
        String original_len_data = "";
        /* 未授权 */
        if(unauthorizedSelected){
            /* 482 */
            List < String > headers_w = analyIRequestInfo.getHeaders();
            /* 484 */
            String[] data_2_list = this.data_2.split("\n");
            /* 485 */
            for(int j = 0; j < headers_w.size(); j++) {
                /* 486 */
                String head_key = ((String) headers_w.get(j)).split(":")[0];
                /* 487 */
                for(int y = 0; y < data_2_list.length; y++) {
                    /* 488 */
                    if(head_key.equals(data_2_list[y])) {
                        /* 489 */
                        headers_w.remove(j);
                        /* 490 */
                        j--;
                    }
                }
            }
            /* 495 */
            if(this.universal_cookie.length() != 0) {
                /* 496 */
                String[] universal_cookies = this.universal_cookie.split("\n");
                /* 497 */
                headers_w.add(headers_w.size() / 2, universal_cookies[0]);
                /* 498 */
                headers_w.add(headers_w.size() / 2, universal_cookies[1]);
            }
            /* 501 */
            byte[] newRequest_w = this.helpers.buildHttpMessage(headers_w, body);
            /* 502 */
            IHttpRequestResponse requestResponse_w = this.callbacks.makeHttpRequest(iHttpService, newRequest_w);
            /* 503 */
            int Unauthorized_len = (requestResponse_w.getResponse()).length -
                    this.helpers.analyzeResponse(requestResponse_w.getResponse()).getBodyOffset();
            /* 504 */

            /* 505 */
            if(original_len == 0) {
                /* 506 */
                original_len_data = Integer.toString(Unauthorized_len);
                /* 507 */
            } else if(original_len == Unauthorized_len) {
                /* 508 */
                original_len_data = Integer.toString(Unauthorized_len) + "  ✔";
            } else {
                /* 510 */
                original_len_data = Integer.toString(Unauthorized_len) + "  ==> " + Integer.toString(original_len - Unauthorized_len);
            }
        }

        /* 514 */
        int id = ++this.conut;
        /* 515 */
        this.log.add(new LogEntry(id, this.helpers.analyzeRequest(baseRequestResponse).getMethod(),
//                this.callbacks.saveBuffersToTempFiles(baseRequestResponse),
//                this.callbacks.saveBuffersToTempFiles(requestResponse_y),
//                this.callbacks.saveBuffersToTempFiles(requestResponse_w),
                null, null,null,
                String.valueOf(this.helpers.analyzeRequest(baseRequestResponse).getUrl()),
                original_len,
                low_len_data,
                original_len_data));
        /* 520 */
        fireTableDataChanged();
        /* 522 */
//        this.logTable.setRowSelectionInterval(this.select_row, this.select_row);
    }
    public List < IScanIssue > doActiveScan(IHttpRequestResponse baseRequestResponse, IScannerInsertionPoint insertionPoint) {
        /* 528 */
        return null;
    }
    public int consolidateDuplicateIssues(IScanIssue existingIssue, IScanIssue newIssue) {
        /* 533 */
        if(existingIssue.getIssueName().equals(newIssue.getIssueName()))
            /* 534 */
            return -1;
        /* 535 */
        return 0;
    }
    public int getRowCount() {
        /* 541 */
        return this.log.size();
    }
    public int getColumnCount() {
        /* 547 */
        return 6;
    }
    public String getColumnName(int columnIndex) {
        /* 553 */
        switch(columnIndex) {
            case 0:
                /* 556 */
                return "#";
            case 1:
                /* 558 */
                return "类型";
            case 2:
                /* 560 */
                return "URL";
            case 3:
                /* 562 */
                return "原始包长度";
            case 4:
                /* 564 */
                return "低权限包长度";
            case 5:
                /* 566 */
                return "未授权包长度";
        }
        /* 568 */
        return "";
    }
    public Class < ? > getColumnClass(int columnIndex) {
        /* 575 */
        return String.class;
    }
    public Object getValueAt(int rowIndex, int columnIndex) {
        /* 581 */
        LogEntry logEntry = this.log.get(rowIndex);
        /* 583 */
        switch(columnIndex) {
            case 0:
                /* 586 */
                return Integer.valueOf(logEntry.id);
            case 1:
                /* 588 */
                return logEntry.Method;
            case 2:
                /* 590 */
                return logEntry.url;
            case 3:
                /* 592 */
                return Integer.valueOf(logEntry.original_len);
            case 4:
                /* 594 */
                return logEntry.low_len;
            case 5:
                /* 596 */
                return logEntry.Unauthorized_len;
        }
        /* 598 */
        return "";
    }
    public byte[] getRequest() {
        /* 607 */
        return this.currentlyDisplayedItem.getRequest();
    }
    public byte[] getResponse() {
        /* 613 */
        return this.currentlyDisplayedItem.getResponse();
    }
    public IHttpService getHttpService() {
        /* 619 */
        return this.currentlyDisplayedItem.getHttpService();
    }
    private class Table
            extends JTable {
        public Table(TableModel tableModel) {
            /* 627 */
            super(tableModel);
        }
        public void changeSelection(int row, int col, boolean toggle, boolean extend) {
            /* 634 */
            BurpExtender.LogEntry logEntry = BurpExtender.this.log.get(row);
            /* 635 */
            BurpExtender.this.select_row = row;
            /* 638 */
            if(col == 4) {
                /* 639 */
                BurpExtender.this.tabs.setSelectedIndex(1);
                /* 640 */
            } else if(col == 5) {
                /* 641 */
                BurpExtender.this.tabs.setSelectedIndex(2);
                /* 642 */
            } else if(col == 3) {
                /* 643 */
                BurpExtender.this.tabs.setSelectedIndex(0);
            }
            /* 646 */
            BurpExtender.this.requestViewer.setMessage(logEntry.requestResponse.getRequest(), true);
            /* 647 */
            BurpExtender.this.responseViewer.setMessage(logEntry.requestResponse.getResponse(), false);
            /* 648 */
            BurpExtender.this.currentlyDisplayedItem = logEntry.requestResponse;
            /* 649 */
            BurpExtender.this.requestViewer_1.setMessage(logEntry.requestResponse_1.getRequest(), true);
            /* 650 */
            BurpExtender.this.responseViewer_1.setMessage(logEntry.requestResponse_1.getResponse(), false);
            /* 651 */
            BurpExtender.this.currentlyDisplayedItem_1 = logEntry.requestResponse_1;
            /* 652 */
            BurpExtender.this.requestViewer_2.setMessage(logEntry.requestResponse_2.getRequest(), true);
            /* 653 */
            BurpExtender.this.responseViewer_2.setMessage(logEntry.requestResponse_2.getResponse(), false);
            /* 654 */
            BurpExtender.this.currentlyDisplayedItem_2 = logEntry.requestResponse_2;
            /* 656 */
            super.changeSelection(row, col, toggle, extend);
        }
    }
    private static class Request_md5 {
        final String md5_data;
        Request_md5(String md5_data) {
            /* 667 */
            this.md5_data = md5_data;
        }
    }
    private static class LogEntry {
        final int id;
        final String Method;
        final IHttpRequestResponsePersisted requestResponse;
        final IHttpRequestResponsePersisted requestResponse_1;
        final IHttpRequestResponsePersisted requestResponse_2;
        final String url;
        final int original_len;
        final String low_len;
        final String Unauthorized_len;
        LogEntry(int id, String Method, IHttpRequestResponsePersisted requestResponse,
                 IHttpRequestResponsePersisted requestResponse_1,
                 IHttpRequestResponsePersisted requestResponse_2,
                 String url, int original_len, String low_len, String Unauthorized_len) {
            /* 687 */
            this.id = id;
            /* 688 */
            this.Method = Method;
            /* 689 */
            this.requestResponse = requestResponse;
            /* 690 */
            this.requestResponse_1 = requestResponse_1;
            /* 691 */
            this.requestResponse_2 = requestResponse_2;
            /* 692 */
            this.url = url;
            /* 693 */
            this.original_len = original_len;
            /* 694 */
            this.low_len = low_len;
            /* 695 */
            this.Unauthorized_len = Unauthorized_len;
        }
    }
    public static String MD5(String key) {
        /* 701 */
        char[] hexDigits = {
                '0',
                '1',
                '2',
                '3',
                '4',
                '5',
                '6',
                '7',
                '8',
                '9',
                'A',
                'B',
                'C',
                'D',
                'E',
                'F'
        };
        try {
            /* 705 */
            byte[] btInput = key.getBytes();
            /* 707 */
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            /* 709 */
            mdInst.update(btInput);
            /* 711 */
            byte[] md = mdInst.digest();
            /* 713 */
            int j = md.length;
            /* 714 */
            char[] str = new char[j * 2];
            /* 715 */
            int k = 0;
            /* 716 */
            for(int i = 0; i < j; i++) {
                /* 717 */
                byte byte0 = md[i];
                /* 718 */
                str[k++] = hexDigits[byte0 >>> 4 & 0xF];
                /* 719 */
                str[k++] = hexDigits[byte0 & 0xF];
            }
            /* 721 */
            return new String(str);
            /* 722 */
        } catch (Exception e) {
            /* 723 */
            return null;
        }
    }
}
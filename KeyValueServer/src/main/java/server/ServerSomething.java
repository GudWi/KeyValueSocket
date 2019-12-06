package server;

import base.BaseDao;

import java.io.*;
import java.net.Socket;
import java.util.regex.Pattern;

class ServerSomething extends Thread {
    private Socket socket;

    private BufferedReader in;
    private BufferedWriter out;

    public ServerSomething(Socket socket) throws IOException {
        this.socket = socket;

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        start();
    }
    @Override
    public void run() {
        Pattern regPut = Pattern.compile("^[pP][uU][tT] <\\w+> <\\w+>$");
        Pattern regGet = Pattern.compile("^[gG][eE][tT] <\\w+>$");
        Pattern regDel = Pattern.compile("^[dD][eE][lL][eE][tT][eE] <\\w+>$");
        BaseDao dao = new BaseDao();

        String word;
        try {
            try {
                while (true) {
                    word = in.readLine();
                    if(word.equals("stop")) {
                        this.downService();
                        break;
                    } else if(regPut.matcher(word).matches()){
                        String[] strings = word.split(" ");

                        dao.putValue(strings[1], strings[2]);
                        word = "Значение сохранено";
                    } else if(regGet.matcher(word).matches()){
                        String[] strings = word.split(" ");

                        word = dao.getValue(strings[1]);
                    } else if(regDel.matcher(word).matches()){
                        String[] strings = word.split(" ");

                        dao.deleteValue(strings[1]);
                        word = "Значение удалено";
                    } else {
                        word = "Текст не подходит под шаблон сообщений";
                    }

                    System.out.println("Echoing: " + word);

                    this.send(word);

                }
            } catch (NullPointerException ignored) {}


        } catch (IOException e) {
            this.downService();
        }
    }

    private void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) {}

    }

    private void downService() {
        try {
            if(!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                for (ServerSomething vr : Server.serverList) {
                    if(vr.equals(this)) vr.interrupt();
                    Server.serverList.remove(this);
                }
            }
        } catch (IOException ignored) {}
    }
}
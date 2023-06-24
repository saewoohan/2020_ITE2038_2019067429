package db4;
import javax.xml.transform.Result;
import java.sql.*;
import java.util.Date;
import java.util.Calendar;
import java.util.Scanner;
import java.lang.*;
import java.util.*;

public class db4 {
    public static void main(String[] args)
    {
        try{
            try{
                Class.forName("org.mariadb.jdbc.Driver");
            }catch(ClassNotFoundException e){
                System.out.println(e);
            }
            Scanner sc = new Scanner(System.in);
            String url = "jdbc:mysql://localhost:3306/db4";
            String user = "root";
            String psw = "53982341do";
            Connection con = DriverManager.getConnection(url,user,psw);
            Statement stmt = con.createStatement();

            Timer m_timer = new Timer();
            TimerTask m_task = new TimerTask(){//Thread use, if Remain == 0 delete user.

                public void run(){
                    String query2 = "UPDATE user set Remain = Remain-1";

                    try{
                        ResultSet rt = stmt.executeQuery(query2); //remain --

                        rt.close();
                        String query8 = "select * from user where Remain =0";
                        ResultSet rq = stmt.executeQuery(query8);
                        while(rq.next()) {
                            String c = rq.getString("UID");
                            String query7 = "alter table db4.enrollment drop foreign key enrollment_playlist";
                            String query6 = "select * from playlist where P_UID = '" + c + "'";
                            ResultSet rf = stmt.executeQuery(query7);
                            rf = stmt.executeQuery(query6);
                            while (rf.next()) {
                                String Pnumber = rf.getString("Pnumber");
                                String query9 = "delete from enrollment where E_Pnumber = '" + Pnumber + "'";
                                ResultSet rg = stmt.executeQuery(query9);
                                rg.close();
                            }
                            String query3 = "delete from playlist where P_UID='" + c + "'";
                            rf = stmt.executeQuery(query3);
                            String query5 = "alter table db4.enrollment add constraint enrollment_playlist foreign key(E_Pnumber) references playlist(Pnumber)";
                            rf = stmt.executeQuery(query5);
                            String query4 = "delete from user where UID='" + c + "'";
                            ResultSet rc = stmt.executeQuery(query4);
                            rf.close();
                            rc.close();
                        }
                        rq.close();
                    }
                    catch(Exception e){

                    }
                }
            };
            m_timer.schedule(m_task,10000,10000);
            int a = 10;

            while(a != 0) {
                System.out.print("DB MUSIC PROGRAM\n\n0. Exit\n1. Manager Menu\n2. User Menu\n\nInput: ");
                a = sc.nextInt();
                System.out.println();


                if(a == 0) { //exit
                    System.out.println("Thanks for using my program.\nBye.\n\n");
                    System.exit(0);
                }


                else if(a == 1) { //manager
                    int M_login = 2;
                    while(M_login != 0) {
                        System.out.print("MANAGER LOGIN MENU\n\n0. Return to previous menu\n1. Login manager mode\n");
                        System.out.print("\nInput: ");
                        M_login = sc.nextInt();

                        if(M_login == 0) {
                            a = 1;
                            System.out.println();
                            break;
                        }
                        else if(M_login == 1) {
                            String c,d;
                            Boolean temp = false;
                            System.out.print("\nID: ");
                            c = sc.nextLine();
                            c = sc.nextLine();
                            System.out.print("Password: ");
                            d = sc.nextLine();
                            String query = "select exists(select * FROM db4.`musicmanager` where MID=" + "'" +c+ "'" +"and Mpassword=" +"'"+d+"') as isChk";

                            ResultSet rs = stmt.executeQuery(query);
                            rs.next();
                            temp = rs.getBoolean(1);
                            rs.close();
                            if(!temp) {
                                System.out.println("Not exist user\n");
                            }
                            else {
                                System.out.println("\nHELLO '"+c+"'");
                                int M_menu = 10;
                                while(M_menu != 0) {
                                    System.out.print("\nMANAGER MENU\n\n0. Return to previous menu\n1. Add music\n2. Delete music\n3. Show all musics\n4. Show my users\n5. Show all users");
                                    System.out.print("\n\nInput: ");

                                    M_menu = sc.nextInt();

                                    if(M_menu == 0) {
                                        System.out.println();
                                        break;
                                    }
                                    else if(M_menu == 1) { //music insert

                                        String M_music,M_publisher,M_genre,M_agency;

                                        System.out.print("\nMusictitle: ");
                                        M_music = sc.nextLine();
                                        M_music = sc.nextLine();
                                        System.out.print("Music publisher: ");
                                        M_publisher = sc.nextLine();
                                        System.out.print("Music genre: ");
                                        M_genre = sc.nextLine();
                                        System.out.print("Music agency: ");
                                        M_agency = sc.nextLine();
                                        String query3 = "SELECT MAX(Musicnum) from music";
                                        ResultSet rf = stmt.executeQuery(query3);
                                        rf.next();
                                        int Musicnum = rf.getInt(1) +1;
                                        rf.close();
                                        String query2 = "INSERT INTO music(Musictitle,Musicpublisher,Genre,Agency,Streamingcount,Mgr_num,Musicnum) VALUES (" + "'" + M_music + "','" + M_publisher + "','" + M_genre + "','" + M_agency + "'," + "0" +",'" + c + "'," + Musicnum +")";
                                        System.out.println("INSERT MUSIC SUCCESS!");
                                        ResultSet rc = stmt.executeQuery(query2);

                                        rc.close();
                                    }

                                    else if(M_menu == 2) { //music delete

                                        String D_music;
                                        System.out.print("Musictitle: ");
                                        D_music = sc.nextLine();
                                        D_music = sc.nextLine();
                                        System.out.print("Musicpublisher: ");
                                        String D_publisher = sc.nextLine();
                                        String query6 = "select * from music where Musictitle='"+D_music+"' and Musicpublisher = '" + D_publisher + "'";
                                        try {
                                            ResultSet rf = stmt.executeQuery(query6);
                                            rf.next();
                                            int Musicnum = rf.getInt("Musicnum");
                                            String query2 = "alter table db4.enrollment drop foreign key enrollment_ibfk_1";
                                            String query3 = "delete from music where Musictitle='" + D_music + "' and Musicpublisher = '" + D_publisher + "'";
                                            String query4 = "delete from enrollment where E_title ='" + Musicnum + "'";
                                            String query5 = "alter table db4.enrollment add foreign key (E_title) references music(Musicnum)";
                                            ResultSet rc = stmt.executeQuery(query2);
                                            rc = stmt.executeQuery(query3);
                                            rc = stmt.executeQuery(query4);
                                            rc = stmt.executeQuery(query5);
                                            System.out.println("Delete music success!");
                                            rc.close();
                                        }catch(Exception e){
                                            System.out.println("Not exists.");
                                        }

                                    }

                                    else if(M_menu == 3){ //Print all music
                                        String query2 = "select * from music";
                                        ResultSet rc = stmt.executeQuery(query2);
                                        while(rc.next()){
                                            String Musictitle,Musicpublisher,Genre,Agency;
                                            int Streamingcount;
                                            Musictitle = rc.getString("Musictitle");
                                            Musicpublisher = rc.getString("Musicpublisher");
                                            Genre = rc.getString("Genre");
                                            Agency = rc.getString("Agency");
                                            Streamingcount = rc.getInt("Streamingcount");

                                            System.out.println("(1) Title: " + Musictitle + "\t(2) Publisher: " + Musicpublisher + "\t(3) Genre: " + Genre + "\t(4) Agency: " + Agency + "\t(5) StreamingCount: " + Streamingcount);


                                        }
                                        rc.close();
                                    }

                                    else if(M_menu == 4) {

                                        String query2 = "select * from user where Mgr_UID='" + c + "'";
                                        ResultSet rc = stmt.executeQuery(query2);
                                        while(rc.next()) {
                                            String Grade, UID,Uphone,Uaddress,Usnumber,Upassword;
                                            Grade = rc.getString("Grade");
                                            UID = rc.getString("UID");
                                            Uphone = rc.getString("Uphone");
                                            Uaddress = rc.getString("Uaddress");
                                            Usnumber = rc.getString("Usnumber");
                                            Upassword = rc.getString("Upassword");

                                            System.out.println("(1) ID: " + UID + "\t(2) Password: " + Upassword+ "\t(3) Grade: " + Grade + "\t(4) Phone number: "+Uphone + "\t(5) Address: " + Uaddress + "\t(6) Social number: "+Usnumber);
                                        }
                                        rc.close();
                                    }

                                    else if(M_menu == 5) {
                                        String query2 = "select * from user";
                                        ResultSet rc = stmt.executeQuery(query2);
                                        while(rc.next()) {
                                            String Grade, UID,Uphone,Uaddress,Usnumber,Upassword,Mgr_UID;
                                            Grade = rc.getString("Grade");
                                            UID = rc.getString("UID");
                                            Uphone = rc.getString("Uphone");
                                            Uaddress = rc.getString("Uaddress");
                                            Usnumber = rc.getString("Usnumber");
                                            Upassword = rc.getString("Upassword");
                                            Mgr_UID = rc.getString("Mgr_UID");
                                            System.out.println("(1) ID: " + UID + "\t(2) Password: " + Upassword+ "\t(3) Grade: " + Grade + "\t(4) Phone number: "+Uphone + "\t(5) Address: " + Uaddress + "\t(6) Social number: "+Usnumber + "\t(7) Manager: " +Mgr_UID);
                                        }
                                        rc.close();
                                    }

                                    else {
                                        System.out.println("Wrong Input!\n");
                                    }
                                }
                            }
                        }
                        else { //error
                            System.out.println("Wrong input!\n");
                        }
                    }
                }



                else if(a == 2) { //user
                    int login = 2;
                    while(login != 0) {


                        System.out.print("\nUSER LOGIN MENU\n\n0. Return to previous menu\n1. Login\n2. Register new user\n3. Find ID\n4. Find Password\n");
                        System.out.print("\nInput: ");
                        login = sc.nextInt();


                        if(login == 0) {
                            a = 1;
                            System.out.println();
                            break;
                        }


                        else if(login == 1) { //login
                            String c,d;
                            String id = null;
                            String password;
                            Boolean temp = false;
                            System.out.print("\nID: ");
                            c = sc.nextLine();
                            c = sc.nextLine();
                            System.out.print("Password: ");
                            d = sc.nextLine();
                            String query = "select exists(select * FROM db4.`user` where UID=" + "'" +c+ "'" +"and Upassword=" +"'"+d+"') as isChk";
                            ResultSet rs = stmt.executeQuery(query);
                            rs.next();
                            temp = rs.getBoolean(1);
                            rs.close();
                            if(!temp) { //login not success
                                System.out.println("Not exist user\n");
                                login = 1;
                            }
                            else { //login success

                                int mainMenu = 10;

                                while(mainMenu != 0) { //mainMenu
                                    System.out.println("\nHELLO '"+c+"'");
                                    System.out.print("\nUSER MAIN MENU\n\n0. Return to previous menu\n1. Listening to music\n2. Services\n3. My information\n");
                                    System.out.print("\nInput: ");
                                    mainMenu = sc.nextInt();

                                    if(mainMenu == 0) {
                                        System.out.println();
                                        break;
                                    }
                                    else if(mainMenu == 1) {
                                        String C_playlist;
                                        ResultSet rd = stmt.executeQuery("SELECT * FROM playlist where P_UID = '"+ c +"' order by Pnumber");
                                        System.out.println("\nPlaylist List\n");
                                        while(rd.next()) {
                                            String playlistname = rd.getString("Pnumber");
                                            int playlistnumber = rd.getInt("Musicnumber");

                                            System.out.println( "(1) playlist: " + playlistname + "\t(2) number: " +playlistnumber);

                                        }
                                        rd.close();
                                        System.out.println("-----------------------------------------------------------------------");
                                        System.out.print("\nChoose playlist: ");
                                        C_playlist = sc.nextLine();
                                        C_playlist = sc.nextLine();
                                        System.out.println();
                                        String query2 = "select exists(select * FROM db4.`playlist` where Pnumber=" + "'" +C_playlist + "' and P_UID = '" + c + "') as isChk";

                                        ResultSet rf = stmt.executeQuery(query2);
                                        rf.next();
                                        Boolean temp2 = rf.getBoolean(1);
                                        if(temp2) { //playlist
                                            String musicMenu;

                                            try{

                                                while(true) {
                                                    String query3 = "select * from enrollment where E_Pnumber='" + C_playlist + "'";
                                                    ResultSet rl = stmt.executeQuery(query3);
                                                    rl.next();
                                                    String musictitle = rl.getString("E_title");
                                                    String query4 = "select * from music where Musictitle='" + musictitle + "'";
                                                    ResultSet ri = stmt.executeQuery(query4);
                                                    while(ri.next()) {
                                                        String Musictitle = ri.getString("Musictitle");
                                                        String Musicpublisher = ri.getString("Musicpublisher");
                                                        String Genre = ri.getString("Genre");
                                                        System.out.println("(1) Musictitle: " + Musictitle +"\t(2) Publisher: " +Musicpublisher+"\t(3) Genre: " + Genre );

                                                    }
                                                    String query5 = "select * from enrollment where E_Pnumber='" + C_playlist + "'";
                                                    rf = stmt.executeQuery(query5);

                                                    while(rf.next()) {

                                                        int musictitle2 = rf.getInt("E_title");
                                                        String query6 = "select * from music where Musicnum='" + musictitle2 + "'";
                                                        rl = stmt.executeQuery(query6);
                                                        while(rl.next()) {String Musictitle = rl.getString("Musictitle");
                                                            String Musicpublisher = rl.getString("Musicpublisher");
                                                            String Genre = rl.getString("Genre");
                                                            String Agency = rl.getString("Agency");
                                                            int Streamingcount = rl.getInt("Streamingcount");
                                                            System.out.println("(1) Musictitle: " + Musictitle +"\t(2) Publisher: " +Musicpublisher+"\t(3) Genre: " + Genre + "\t(4) Agency: "+Agency+"\t(5) StreamingCount: "+Streamingcount );
                                                        }
                                                        rl.close();

                                                    }
                                                    rf.close();
                                                    System.out.println("---------------------------------------------------------------------------------------------------------------");
                                                    System.out.print("Choose music(0 is quit): ");
                                                    musicMenu = sc.nextLine();
                                                    if(musicMenu.equals("0")) { //stop music
                                                        System.out.println("Stop listening music!");
                                                        break;
                                                    }
                                                    else {
                                                        String query6 = "select exists(select * FROM db4.`music` where Musictitle='" + musicMenu +"') as isChk";
                                                        ResultSet rp = stmt.executeQuery(query6);
                                                        rp.next();
                                                        Boolean temp3 = rp.getBoolean(1);
                                                        rp.close();
                                                        if(temp3) { //have music
                                                            System.out.println("Listening...\n");
                                                            Thread.sleep(5);
                                                            String query7 = "update music set Streamingcount=Streamingcount+1 where Musictitle='"+musicMenu+"'";
                                                            ResultSet rq = stmt.executeQuery(query7);
                                                            rq.close();
                                                        }
                                                        else { //no music
                                                            System.out.println("Not exists.");
                                                        }
                                                    }

                                                }
                                            }
                                            catch(Exception e) {
                                                System.out.println("Not exists music.");
                                            }


                                        }
                                        else { //no playlist
                                            System.out.println("Not exists.");
                                        }
                                    }
                                    else if(mainMenu == 2) { //service

                                        int userMenu = 10;
                                        while(userMenu!=0) {

                                            System.out.print("\nUSER SERVICE MENU\n\n0. Return to previous menu\n1. Make PlayList\n2. Delete PlayList\n3. Add Music\n4. Delete Music\n5. Show my PlayLists\n6. Show musics in my Playlist\n7. Show Music chart\n");
                                            System.out.print("\nInput: ");
                                            userMenu = sc.nextInt();

                                            if(userMenu == 0) { //exit
                                                System.out.println();
                                                break;
                                            }

                                            else if(userMenu == 1) { //Playlist make
                                                System.out.print("\nInsert Playlist name: ");
                                                String trash = sc.nextLine();
                                                String pname = sc.nextLine();
                                                String query2 = "INSERT INTO playlist(Pnumber,Prank,Musicnumber,P_UID) VALUES (" + "'" + pname + "'" + "," + "-1" +"," + "0" + ","  + "'"+ c +"'"+ ")";


                                                try{
                                                    ResultSet rc = stmt.executeQuery(query2);
                                                    System.out.println("Make Successful!");
                                                    rc.close();
                                                }

                                                catch (Exception e){
                                                    System.out.println("Already exist.");
                                                }


                                            }

                                            else if(userMenu == 2){
                                                System.out.print("\nChoose playList: ");
                                                String trash2 = sc.nextLine();
                                                String mplaylist = sc.nextLine();
                                                String query2 = "alter table db4.enrollment drop foreign key enrollment_playlist";
                                                String query6 = "select * from playlist where Pnumber = '" + mplaylist +"'";
                                                ResultSet rf = stmt.executeQuery(query2);
                                                rf = stmt.executeQuery(query6);
                                                rf.next();
                                                String query7 = "delete from enrollment where E_Pnumber = '" + mplaylist +"'";
                                                ResultSet rg = stmt.executeQuery(query7);
                                                rg.close();
                                                String query3 = "delete from playlist where Pnumber='"+ mplaylist +"'" ;
                                                rf = stmt.executeQuery(query3);
                                                String query5 = "alter table db4.enrollment add constraint enrollment_playlist foreign key(E_Pnumber) references playlist(Pnumber)";
                                                rf = stmt.executeQuery(query5);
                                                System.out.println("Delete success!");
                                            }

                                            else if(userMenu == 3) { //add music in playlist
                                                System.out.print("\nChoose playlist: ");
                                                String trash2 = sc.nextLine();
                                                String mplaylist = sc.nextLine();
                                                try {
                                                    String query6 = "select * from playlist where Pnumber = '" + mplaylist + "'";
                                                    ResultSet rq = stmt.executeQuery(query6);
                                                    rq.next();
                                                    String P_ID = rq.getString("P_UID");
                                                    rq.close();

                                                    if (P_ID.equals(c)) {
                                                        System.out.println();
                                                        String query5 = "select * from music";
                                                        ResultSet rc = stmt.executeQuery(query5);
                                                        while (rc.next()) {
                                                            String Musictitle, Musicpublisher, Genre, Agency;
                                                            int Streamingcount;
                                                            Musictitle = rc.getString("Musictitle");
                                                            Musicpublisher = rc.getString("Musicpublisher");
                                                            Genre = rc.getString("Genre");
                                                            Agency = rc.getString("Agency");
                                                            Streamingcount = rc.getInt("Streamingcount");

                                                            System.out.println("(1) Title: " + Musictitle + "\t(2) Publisher: " + Musicpublisher + "\t(3) Genre: " + Genre + "\t(4) Agency: " + Agency + "\t(5) StreamingCount: " + Streamingcount);

                                                        }
                                                        System.out.println("---------------------------------------------------------------------------------------------------------------");
                                                        System.out.print("Choose music title: ");
                                                        String mmusic = sc.nextLine();
                                                        System.out.print("Choose music publisher: ");
                                                        String mpub = sc.nextLine();
                                                        String query3 = "SELECT * from music where Musictitle='" + mmusic + "' and Musicpublisher ='" + mpub + "'";
                                                        try {
                                                            rc = stmt.executeQuery(query3);
                                                            rc.next();
                                                            int Musicnum = rc.getInt("Musicnum");
                                                            rc.close();
                                                            String query2 = "INSERT INTO enrollment(E_Pnumber, E_title) VALUES ('" + mplaylist + "'," + Musicnum + ")";
                                                            ResultSet rx = stmt.executeQuery(query2);
                                                            System.out.println("Add succssful!");
                                                            String query4 = "UPDATE playlist set Musicnumber = Musicnumber+1 where Pnumber = '" + mplaylist + "' and P_UID = '" + c + "'";
                                                            rx = stmt.executeQuery(query4);
                                                            rx.close();
                                                        } catch (Exception e) {
                                                            System.out.println("Not exists or Already exists.");
                                                        }
                                                    } else {
                                                        System.out.println("Not exists.");
                                                    }
                                                }catch (Exception e){
                                                    System.out.println("Not exists.");
                                                }


                                            }

                                            else if(userMenu == 4){ //delete music
                                                System.out.print("\nChoose playlist: ");
                                                String trash2= sc.nextLine();
                                                String mplaylist = sc.nextLine();
                                                try {
                                                    String query7 = "select * from playlist where Pnumber = '" + mplaylist + "'";
                                                    ResultSet rq = stmt.executeQuery(query7);
                                                    rq.next();
                                                    String P_ID = rq.getString("P_UID");
                                                    rq.close();
                                                    if(P_ID.equals(c)) {
                                                        String query5 = "select * from enrollment where E_Pnumber='" + mplaylist + "'";
                                                        ResultSet rf = stmt.executeQuery(query5);
                                                        System.out.println("\n------------------ Music List -------------------\n");

                                                        while (rf.next()) {

                                                            int musictitle = rf.getInt("E_title");
                                                            String query6 = "select * from music where Musicnum='" + musictitle + "'";
                                                            ResultSet rl = stmt.executeQuery(query6);
                                                            while (rl.next()) {
                                                                String Musictitle = rl.getString("Musictitle");
                                                                String Musicpublisher = rl.getString("Musicpublisher");
                                                                String Genre = rl.getString("Genre");
                                                                String Agency = rl.getString("Agency");
                                                                int Streamingcount = rl.getInt("Streamingcount");
                                                                System.out.println("(1) Musictitle: " + Musictitle + "\t(2) Publisher: " + Musicpublisher + "\t(3) Genre: " + Genre + "\t(4) Agency: " + Agency + "\t(5) StreamingCount: " + Streamingcount);
                                                            }
                                                            rl.close();

                                                        }
                                                        rf.close();
                                                        System.out.println("\n---------------------------------------------------------------------------------------------------------------");
                                                        System.out.print("Choose music title: ");
                                                        String mmusic = sc.nextLine();
                                                        System.out.print("Choose music publisher: ");
                                                        String mpub = sc.nextLine();
                                                        String query3 = "SELECT * from music where Musictitle='" + mmusic + "' and Musicpublisher ='" + mpub + "'";
                                                        ResultSet rc = stmt.executeQuery(query3);
                                                        rc.next();
                                                        int Musicnum = rc.getInt("Musicnum");
                                                        rc.close();
                                                        String query2 = "Delete from enrollment where E_Pnumber='" + mplaylist + "' and E_title = " + Musicnum;
                                                        ResultSet rx = stmt.executeQuery(query2);
                                                        rx = stmt.executeQuery(query2);
                                                        String query4 = "UPDATE playlist set Musicnumber = Musicnumber-1 where Pnumber = '" + mplaylist + "'";
                                                        rx = stmt.executeQuery(query4);
                                                        rx.close();
                                                        System.out.println("Delete success!");
                                                    }
                                                    else{
                                                        System.out.println("Not exists.");
                                                    }
                                                }catch(Exception e){
                                                    System.out.println("Not exists.");
                                                }
                                            }

                                            else if(userMenu == 5) { //show playlist
                                                System.out.println("\n----------------- Playlist List ------------------\n");
                                                ResultSet rd = stmt.executeQuery("SELECT * FROM playlist where P_UID='" + c +"' order by Pnumber");
                                                while(rd.next()) {
                                                    String playlistname = rd.getString("Pnumber");
                                                    int playlistnumber = rd.getInt("Musicnumber");

                                                    System.out.println( "(1) playlist: " + playlistname + "\t(2) number: " +playlistnumber);

                                                }
                                                rd.close();
                                            }

                                            else if(userMenu == 6) { //show musics in playlist

                                                System.out.print("\nChoose playlist: ");

                                                String trash3 = sc.nextLine();
                                                String Splaylist = sc.nextLine();


                                                String query2 = "select * from enrollment,playlist where E_Pnumber='" + Splaylist + "' and Pnumber = '" + Splaylist + "'and P_UID = '" +c+ "'";
                                                ResultSet rf = stmt.executeQuery(query2);
                                                System.out.println("\n------------------ Music List -------------------\n");

                                                while(rf.next()) {

                                                    int musictitle = rf.getInt("E_title");
                                                    String query3 = "select * from music where Musicnum='" + musictitle + "'";
                                                    ResultSet rl = stmt.executeQuery(query3);
                                                    while(rl.next()) {String Musictitle = rl.getString("Musictitle");
                                                        String Musicpublisher = rl.getString("Musicpublisher");
                                                        String Genre = rl.getString("Genre");
                                                        String Agency = rl.getString("Agency");
                                                        int Streamingcount = rl.getInt("Streamingcount");
                                                        System.out.println("(1) Musictitle: " + Musictitle +"\t(2) Publisher: " +Musicpublisher+"\t(3) Genre: " + Genre + "\t(4) Agency: "+Agency+"\t(5) StreamingCount: "+Streamingcount );
                                                    }
                                                    rl.close();

                                                }
                                                rf.close();
                                            }

                                            else if(userMenu == 7) { //music chart
                                                int i =0;
                                                String query2 ="select * from music order by Streamingcount desc";
                                                ResultSet rd = stmt.executeQuery(query2);
                                                System.out.println("\n------------------ MUSIC CHART -------------------\n");
                                                while(rd.next()) {

                                                    i++;
                                                    String Musictitle = rd.getString("Musictitle");
                                                    String Musicpublisher = rd.getString("Musicpublisher");
                                                    int Streamingcount = rd.getInt("Streamingcount");
                                                    System.out.println(i + ". " + Musictitle +"(" +Musicpublisher + ")\tStreaming Count: " + Streamingcount);

                                                }

                                                rd.close();
                                            }

                                            else {
                                                System.out.println("Wrong Input!");
                                            }
                                        }
                                    }
                                    else if(mainMenu == 3) { //User Information
                                        int infoMenu = 10;

                                        while(infoMenu != 0){

                                            System.out.println("\nINFORMATION MENU\n\n0. Return to previous menu\n1. My Information\n2. Modify Information\n3. Regular payment\n4. Account Withdrawal");
                                            System.out.print("\nInput: ");
                                            infoMenu = sc.nextInt();
                                            if(infoMenu == 0){
                                                System.out.println();
                                                break;
                                            }
                                            else if (infoMenu == 1){
                                                String query2 = "Select * from user where UID= '"+ c +"'";
                                                ResultSet rd = stmt.executeQuery(query2);
                                                rd.next();
                                                String Grade,UID,Uphone,Uaddress,Usnumber;
                                                int Remain;
                                                Grade = rd.getString("Grade");
                                                UID = rd.getString("UID");
                                                Uphone = rd.getString("Uphone");
                                                Uaddress = rd.getString("Uaddress");
                                                Usnumber = rd.getString("Usnumber");
                                                Remain = rd.getInt("Remain");

                                                System.out.println("\nInformation\n\n(1) Grade: "+Grade +"\n(2) ID: "+UID + "\n(3) Phone number: "+Uphone + "\n(4) Address: " + Uaddress + "\n(5) Social number: "+Usnumber +"\n(6) Remain subscribe days: "+Remain);
                                            }
                                            else if (infoMenu == 2){ //Modify
                                                int modiInfo = 10;
                                                while(modiInfo != 0){
                                                    System.out.println("\nINFORMATION MODIFY MENU\n\n0. Return to previous menu\n1. Modify phone number\n2. Modify address\n3. Modify password");
                                                    System.out.print("\nInput: ");
                                                    modiInfo = sc.nextInt();
                                                    if(modiInfo == 0){
                                                        System.out.println();
                                                        break;
                                                    }
                                                    else if (modiInfo == 1){
                                                        System.out.print("Input phone number: ");
                                                        String Mphone = sc.nextLine();
                                                        Mphone = sc.nextLine();
                                                        String query2 = "update user set Uphone = '" + Mphone + "' where UID='" + c +"'";
                                                        ResultSet rf = stmt.executeQuery(query2);
                                                        System.out.println("Modify success!");
                                                        rf.close();
                                                    }
                                                    else if (modiInfo == 2){
                                                        System.out.print("Input address: ");
                                                        String Address = sc.nextLine();
                                                        Address = sc.nextLine();
                                                        String query2 = "update user set Uaddress = '" + Address + "'where UID ='" +c +"'";
                                                        ResultSet rf = stmt.executeQuery(query2);
                                                        System.out.println("Modify success!");
                                                        rf.close();
                                                    }
                                                    else if (modiInfo == 3){
                                                        while(true) {
                                                            System.out.print("Input current password: ");
                                                            String Cpassword = sc.nextLine();
                                                            Cpassword = sc.nextLine();
                                                            if (d.equals(Cpassword)) {
                                                                System.out.print("Input password: ");
                                                                String Password = sc.nextLine();
                                                                String query2 = "update user set Upassword = '" + Password + "'where UID = '" +c +"'";
                                                                ResultSet rf = stmt.executeQuery(query2);
                                                                System.out.println("Modify success!");
                                                                rf.close();
                                                                break;
                                                             }
                                                            else{
                                                                System.out.println("Wrong Password!");
                                                            }
                                                        }
                                                    }
                                                    else{
                                                        System.out.println("Wrong Input!");
                                                    }
                                                }
                                            }
                                            else if (infoMenu == 3){ //Regular payment
                                                String payMenu;
                                                System.out.println("\nA month subscription ticket costs is $5.");

                                                System.out.println("Would you like to pay?(y or n)\n");
                                                payMenu = sc.nextLine();
                                                payMenu = sc.nextLine();
                                                if(payMenu.equals("y")){
                                                    System.out.println("Payment in progress\n");
                                                    Thread.sleep(10);
                                                    String query2 = "update user set Remain = Remain +30,Paynumber = Paynumber +1 where UID = '" + c +"'";
                                                    ResultSet rf = stmt.executeQuery(query2);
                                                    System.out.println("Payment Success!");
                                                    String query3 = "select * from user where UID = '" + c +"'";
                                                    rf = stmt.executeQuery(query3);
                                                    rf.next();

                                                    int Paynumber = rf.getInt("Paynumber");
                                                    if(Paynumber == 6){
                                                        String query4 = "update user set Grade = 'Silver' where UID = '" + c + "'";
                                                        rf = stmt.executeQuery(query4);
                                                        System.out.println("Your grade is now 'Silver'!");
                                                    }
                                                    else if(Paynumber == 12){
                                                        String query4 = "update user set Grade = 'Gold' where UID = '" + c + "'";
                                                        rf = stmt.executeQuery(query4);
                                                        System.out.println("Your grade is now 'Gold'!");
                                                    }
                                                    else if(Paynumber == 36){
                                                        String query4 = "update user set Grade = 'Platinum' where UID = '" + c + "'";
                                                        rf = stmt.executeQuery(query4);
                                                        System.out.println("Your grade is now 'Platinum'!");
                                                    }
                                                    else if(Paynumber == 60){
                                                        String query4 = "update user set Grade = 'Diamond' where UID = '" + c + "'";
                                                        rf = stmt.executeQuery(query4);
                                                        System.out.println("Your grade is now 'Diamond'!");
                                                    }

                                                    rf.close();
                                                }
                                                else{
                                                    System.out.println();
                                                }

                                            }
                                            else if (infoMenu == 4){ //account delete
                                                String deleteMenu;
                                                System.out.println("Warning!!\nAre you sure you want to delete it?(y or n)");
                                                deleteMenu = sc.nextLine();
                                                deleteMenu = sc.nextLine();



                                                if(deleteMenu.equals("y")){
                                                    System.out.println("Delete in progress\n");
                                                    String query2 = "alter table db4.enrollment drop foreign key enrollment_playlist";
                                                    String query6 = "select * from playlist where P_UID = '" + c + "'";
                                                    ResultSet rf = stmt.executeQuery(query2);
                                                    rf = stmt.executeQuery(query6);
                                                    while(rf.next()){
                                                        String Pnumber = rf.getString("Pnumber");
                                                        String query7 = "delete from enrollment where E_Pnumber = '" + Pnumber +"'";
                                                        ResultSet rg = stmt.executeQuery(query7);
                                                        rg.close();
                                                    }
                                                    String query3 = "delete from playlist where P_UID='"+ c +"'" ;
                                                    rf = stmt.executeQuery(query3);
                                                    String query5 = "alter table db4.enrollment add constraint enrollment_playlist foreign key(E_Pnumber) references playlist(Pnumber)";
                                                    rf = stmt.executeQuery(query5);
                                                    String query4 = "delete from user where UID='" + c + "'";
                                                    ResultSet rc = stmt.executeQuery(query4);
                                                    System.out.println("Delete Success!\nBye.");
                                                    rf.close();
                                                    rc.close();
                                                    mainMenu = 0;
                                                    break;
                                                }
                                                else{
                                                    System.out.println();
                                                }
                                            }
                                            else{
                                                System.out.println("Wrong Input!");
                                            }
                                        }
                                    }
                                    else {
                                        System.out.println("Wrong Input!");
                                    }
                                }
                            }
                        }


                        else if(login == 2) { //make u ID
                            String e = null,f = null;
                            Boolean already = true;
                            while(already) {
                                System.out.print("ID: ");
                                e = sc.next();
                                System.out.print("Password: ");
                                f = sc.next();
                                String query = "select exists(select * FROM db4.`user` where UID=" + "'" +e+ "') as isChk";
                                ResultSet rq = stmt.executeQuery(query);
                                rq.next();
                                already = rq.getBoolean(1);
                                if(already) {
                                    System.out.println("Already registered ID.\n");
                                }
                            }
                            System.out.print("Phonenum: ");
                            String g = sc.next();
                            System.out.print("Social number: ");
                            String h = sc.next();
                            System.out.print("Address: ");
                            String trash3 = sc.nextLine();
                            String j = sc.nextLine();
                            String query3 = "select MID from musicmanager order by rand() limit 1";
                            ResultSet rm = stmt.executeQuery(query3);
                            rm.next();
                            String managerID = rm.getString("MID");
                            String query2 = "INSERT INTO user(Grade,UID,Upassword,Uphone,Usnumber,Uaddress,Mgr_UID,Remain) VALUES ('Bronze','"+ e +"','" + f+"','"+g+"','"+h+"','"+j+ "','" + managerID +"',30)";
                            ResultSet rp = stmt.executeQuery(query2);
                            System.out.println("MAKE ID SUCCESS!");
                            rm.close();
                            rp.close();
                        }

                        else if (login == 3){
                            String Uphone;
                            System.out.print("\nInput phone number: ");
                            Uphone = sc.nextLine();
                            Uphone = sc.nextLine();
                            try {
                                String query2 = "select * from user where Uphone = '" + Uphone + "'";
                                ResultSet rm = stmt.executeQuery(query2);

                                rm.next();

                                String UID = rm.getString("UID");
                                System.out.println("Your ID is '" + UID + "'");
                                rm.close();
                            }
                            catch (Exception e){
                                System.out.println("Not exists.");
                            }
                        }
                        else if (login == 4){
                            String Uphone, UID;
                            System.out.print("\nInput your ID: ");
                            UID = sc.nextLine();
                            UID = sc.nextLine();
                            System.out.print("Input phone number: ");

                            Uphone = sc.nextLine();

                            try{
                                String query2 = "select * from user where Uphone = '" + Uphone + "' and UID = '" + UID +"'";
                                ResultSet rm = stmt.executeQuery(query2);
                                rm.next();
                                String Upassword = rm.getString("Upassword");
                                System.out.println("Your Password is '" + Upassword + "'");
                            }catch(Exception e){
                                System.out.println("Not exists.");
                            }
                        }
                        else{
                            System.out.println("Wrong Input!");
                        }

                    }
                }


                else {//error
                    System.out.println("Wrong Input!");
                    break;
                }


            }



        }catch(Exception e) {
            System.out.println("Abnormal approach\nExit program.");
            System.exit(0);
        }

    }
}
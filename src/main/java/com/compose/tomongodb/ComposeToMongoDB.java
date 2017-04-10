package com.compose.tomongodb;

import com.github.jkutner.EnvKeyStore;
import com.mongodb.*;
import com.mongodb.client.*;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import org.bson.Document;
import java.util.ArrayList;
import java.util.logging.*;
import java.util.logging.Logger;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class ComposeToMongoDB {

    public static void main(String[] args) {
        try {
            KeyStore ts = EnvKeyStore.createWithRandomPassword("TRUSTED_CERT").keyStore();
            
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(ts);
            
            SSLContext sc = SSLContext.getInstance("TLSv1.2");
            sc.init(null, tmf.getTrustManagers(), new SecureRandom());
            
            MongoClientOptions.Builder mco=MongoClientOptions.builder().socketFactory(sc.getSocketFactory());
            MongoClientURI connectionString = new MongoClientURI("mongodb://javauser:javapass@aws-eu-west-1-portal.0.dblayer.com:10980,aws-eu-west-1-portal.2.dblayer.com:10165/javatester?ssl=true",mco);
            MongoClient mongoClient = new MongoClient(connectionString);
            MongoDatabase mongoDatabase = mongoClient.getDatabase("javatester");
            MongoCollection mongoCollection = mongoDatabase.getCollection("javatester");
            ArrayList<Document> arrayList = new ArrayList<>();
            for (int i = 0; i < 100; ++i) {
                arrayList.add(new Document("i", (Object) i));
            }
            mongoCollection.insertMany(arrayList);
            System.out.println(mongoCollection.count());
        } catch (KeyManagementException | IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException ex) {
            Logger.getLogger(ComposeToMongoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

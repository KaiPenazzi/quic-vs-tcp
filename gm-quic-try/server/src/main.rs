use gm_quic::BuildServerError;
use tokio::io::AsyncReadExt;

#[tokio::main]
async fn main() -> Result<(), BuildServerError> {
    let quic_listeners = gm_quic::QuicListeners::builder()?
        .without_client_cert_verifier()
        .listen(8192);

    // Add a server that can be connected
    let _ = quic_listeners
        .add_server(
            "cooler_server",
            include_bytes!("../server.crt"),
            include_bytes!("../server.key"),
            ["127.0.0.1:7000"],
            None,
        )
        .expect("could not add server");

    while let Ok((connection, server_name, pathway, link)) = quic_listeners.accept().await {
        tokio::spawn(async move {
            // Die Stream-Handling-Schleife. Sie läuft, solange die Verbindung offen ist und Streams akzeptiert werden können.
            loop {
                match connection.accept_uni_stream().await {
                    Ok((id, mut stream)) => {
                        println!("📥 Incoming uni stream id: {}", id);

                        let mut buffer = Vec::new();
                        if let Ok(_) = stream.read_to_end(&mut buffer).await {
                            let payload = String::from_utf8_lossy(&buffer);
                            println!("🧾 Received payload: {}", payload);
                            // Nach erfolgreichem Lesen des Streams ist dieser Stream beendet,
                            // der Loop kehrt zurück und wartet auf den nächsten Stream.
                        } else {
                            eprintln!("⚠️ Failed to read from stream {}", id);
                            // Bei einem Lesefehler wird die Stream-Schleife beendet.
                            break;
                        }
                    }
                    // Wenn `accept_uni_stream` fehlschlägt oder die Verbindung geschlossen wird, wird hier ein Fehler (oder None) zurückgegeben.
                    // Das ist das Signal, dass wir die `tokio::spawn` Task beenden müssen.
                    Err(e) => {
                        eprintln!("❌ Connection closed or error accepting stream: {:?}", e);
                        break;
                    }
                }
            }

            // WICHTIG: Warte auf das Schließen der Verbindung, um die Task zu beenden.
            // Die Connection ist ein Future, das abgeschlossen wird, wenn die Verbindung geschlossen ist.
            println!("🛑 Connection handler für {} beendet.", server_name);
        });
    }

    Ok(())
}

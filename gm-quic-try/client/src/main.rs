use gm_quic::{self, EndpointAddr, ToCertificate};
use std::{
    io::Result,
    net::{IpAddr, SocketAddr},
    path::PathBuf,
    str::FromStr,
};
use tokio::io::AsyncWriteExt;

#[tokio::main]
async fn main() -> Result<()> {
    let mut roots = rustls::RootCertStore::empty();
    // roots.add_parsable_certificates(rustls_native_certs::load_native_certs().certs);
    roots.add_parsable_certificates(PathBuf::from("../server/server.crt").to_certificate());

    let quic_client = gm_quic::QuicClient::builder()
        .with_root_certificates(roots)
        .without_cert()
        .build();

    let server_addr = SocketAddr::new(IpAddr::from_str("127.0.0.1").unwrap(), 7000);
    let connection = quic_client.connect("cooler_server", [server_addr])?; // `mut` hinzugefügt

    let (id, mut stream) = connection.open_uni_stream().await?.unwrap();

    println!("id: {}", id);

    let _ = stream.write_all(b"idk").await;
    // WICHTIG: Schließe den Stream, nachdem alle Daten gesendet wurden (End of Stream Signal)
    let _ = stream.shutdown().await;

    // Schließe die gesamte Verbindung
    connection.close("finish", 200);

    println!("✅ Client Finished and connection closed.");
    Ok(())
}

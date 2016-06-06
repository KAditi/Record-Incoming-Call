Set up environment:

1. Use eclipse integrated with adt bundle.
2. Import project from dependancy folder and Call Tracker project in workspace
3. Go to AudioRecorder service class. Change server IP to desired server.
4. Build project
5. Run project as android application
6. Install .apk file on desired device (emulator / smartphone)
7. Install PHP on server where you want to upload audio files
8. Keep UploadToServer.php file in www folder and create "uploads" folder in www
9. On incoming call or outgoing call event audio recorder will be invoked. It will record ongoing conversation and send it to server once call is terminated. 
10. You will find recorded files in uploads folder




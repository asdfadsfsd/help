import React, { useEffect, useRef } from 'react';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';

const CHUNK_SIZE = 16304; // Set the maximum chunk size (adjust as needed)

const Login = () => {
    const stompClientRef = useRef(null);
    const audioElementRef = useRef(null);
    const roomId = 1;
    const userId = 1 //+ Math.ceil(Math.random() * 99);
    
    useEffect(() => {
        const socket = new SockJS('http://localhost:9090/ws');
        const stompClient = Stomp.over(socket);
        stompClient.connect({}, async function(frame) {
            console.log('Connected: ' + frame);

            // Subscribe to audio messages from the server
            stompClient.subscribe(`/topic/audio/${roomId}/${userId}`, function(message) {
                console.log(message)
                const audioData = new Uint8Array(message.binaryBody);
                playReceivedAudio(audioData);
            });
        }, function(error) {
            console.error('Connection error: ' + error); // Handle connection errors
        });

        stompClientRef.current = stompClient;

        // Handle disconnection on window unload
        window.onbeforeunload = function() {
            stompClient.disconnect();
        };

    }, []);

    const handleFileChange = (event) => {
        const file = event.target.files[0];
        if (file) {
            sendAudio(file);
        }
    };

    const sendAudio = (file) => {
        const reader = new FileReader();
        
        reader.onload = (e) => {
            const audioData = new Uint8Array(e.target.result);
            sendChunks(audioData);
        };

        reader.onerror = (error) => {
            console.error('Error reading file:', error);
        };

        reader.readAsArrayBuffer(file);
    };

    const sendChunks = (audioData) => {
        const audioBlob = new Blob([audioData], { type: 'audio/opus' });
        console.log(audioBlob)
        const stompClient = stompClientRef.current;
        const totalChunks = Math.ceil(audioData.byteLength /  CHUNK_SIZE);
        // Loop to send chunks of the audio data
        for (let i = 0; i < totalChunks; i ++) {
            const start = i * CHUNK_SIZE;
            const end = Math.min(start + CHUNK_SIZE, audioData.byteLength);
            const chunk = audioData.slice(start, end);
            // Add null octet at the end of the chunk
            const chunkWithNull = new Uint8Array([...chunk], null);
            const destination = `/app/audio-send/${roomId}/${userId}`;    
            try {
                if (stompClient && stompClient.connected) {
                    stompClient.send(
                        destination,
                        {
                            
                        },
                        chunkWithNull
                    );
                    break
                } else {
                    throw new Error('WebSocket is not connected.');
                }
            } catch (error) {
                console.error('Failed to send audio chunk:', error);
            }
        }
    };

    const playReceivedAudio = (audioData) => {
        const audioBlob = new Blob([audioData], { type: 'audio/opus' });
        const audioUrl = URL.createObjectURL(audioBlob);
        const audioElement = audioElementRef.current;

        if (audioElement) {
            audioElement.src = audioUrl;
            audioElement.play().catch((error) => {
                console.error('Error playing audio:', error);
            });
        }
    };

    return (
        <div>
            <h2>Audio Chat Test (MP3 to Opus)</h2>
            <input type="file" accept="audio/mp3" onChange={handleFileChange} />
            <audio ref={audioElementRef} controls />
        </div>
    );
};

export default Login;
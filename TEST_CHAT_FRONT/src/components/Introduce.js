import React ,{ useEffect, useState, useRef }  from 'react'
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import axios from 'axios';
import { forEach } from 'sockjs-client/lib/transport-list';


const  Introduce = () => {
    const [mediaRecorder, setMediaRecorder] = useState(null);
    const [isRecording, setIsRecording] = useState(false);
    const [stompClient, setStompClient] = useState(null);

    useEffect(() => {
        // SockJS와 Stomp 클라이언트 초기화
        const socket = new SockJS('http://localhost:9090/ws'); // 서버 URL에 맞게 수정
        const client = Stomp.over(socket);

        client.connect({}, () => {
            console.log('연결됨');
            setStompClient(client);
        });

        return () => {
            if (client) {
                client.disconnect();
            }
        };
    }, []);

    const startRecording = async () => {
        const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
        const recorder = new MediaRecorder(stream);

        recorder.ondataavailable = async (event) => {
            if (stompClient) {
                const audioBlob = event.data; // Blob 객체
                const arrayBuffer = await audioBlob.arrayBuffer(); // Blob을 ArrayBuffer로 변환
                const audioData= new Uint8Array(arrayBuffer)
                // 헤더 추가
                const headers = {
                    'Content-Type': 'application/octet-stream',
                    'Content-Length': audioData.length // Set content length in bytes
                };
                stompClient.send(`/app/audio-send/1/1`, headers, audioData);
            }
        };

        recorder.start(100); // 100ms마다 데이터 전송
        setMediaRecorder(recorder);
        setIsRecording(true);
    };

    const stopRecording = () => {
        mediaRecorder.stop();
        setIsRecording(false);
    };

    return (
        <div>
            <h1>오디오 녹음기</h1>
            <button onClick={startRecording} disabled={isRecording}>녹음 시작</button>
            <button onClick={stopRecording} disabled={!isRecording}>녹음 중지</button>
        </div>
    );
}

export default Introduce





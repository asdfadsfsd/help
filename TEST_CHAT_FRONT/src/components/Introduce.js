import React ,{ useEffect, useState, useRef }  from 'react'
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import axios from 'axios';
import { forEach } from 'sockjs-client/lib/transport-list';


const  Introduce = () => {
  const socket = new SockJS('http://localhost:9090/ws'); // 서버 주소에 맞게 수정
  const stompClient = Stomp.over(socket);
  const room_id =1

  useEffect(()=>{
        stompClient.connect({}, async function(frame) {
          console.log('Connected: ' + frame);
           
          //TEST 서버에 저장된  캐쉬데이터 챙겨와보기
          const initData = await axios.get("http://localhost:9090/room/enter/1",{
            params:{
                user_id: 1,
                last_chat: null
            }
          })

          console.log(initData.data.items)
          const initMsg = initData;
          console.log(initMsg)
    
          // 서버로부터 메시지를 수신하기 위한 구독 설정
          stompClient.subscribe(`/topic/chat/1`, function(message) {
              const msg = JSON.parse(message.body);
              console.log(msg)
              document.getElementById('messages').innerHTML += `<div>${msg.message}</div>`;
          });
      }, function(error) {
          console.error('Connection error: ' + error); // 연결 오류 처리
      });

      // 메시지 전송 기능
      document.getElementById('send').addEventListener('click', function() {
          const input = document.getElementById('input');
          const connectTest = input.value;
          const currentDate = new Date();
          //test message in room
          const test_chat ={
              roomId:1,
              userId:1,
              message:"SIMPLE_MESSAGE :"+currentDate.getMilliseconds()
          }
          
          // 메시지 전송
          stompClient.send("/app/join", {}, JSON.stringify(test_chat));
          
          // 입력창 초기화
          input.value = '';
      });

      // 연결 종료 시 처리
      window.onbeforeunload = function() {
          stompClient.disconnect();
      };


  },[])
  
  return (
    <>
    <h1>WebSocket Chat</h1>
    <div id="messages"></div>
    <input type="text" id="input" placeholder="메시지를 입력하세요..." />
    <button id="send">전송</button>
</>
  )
}

export default Introduce





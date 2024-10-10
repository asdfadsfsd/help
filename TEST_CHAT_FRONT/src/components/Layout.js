import React from 'react'
import { Outlet, useNavigate } from 'react-router-dom'

const Layout = () => {
  //react router dom 에서 useNavigate 라는 ㅙㅐㅏdmfh
   const navi = useNavigate();
   const movePage = (url)=>{
    navi(url);
   }
  return (
    <div>
      <header style={{background: "lightgray" , padding: "16px",fontSize : "24px"}}>
        <button type='button' onClick={()=>{navi(-1)}}>Go Back</button>
        <button type='button' onClick={()=>{navi("/")}}>Home</button>
        <button type='button' onClick={()=>{navi("/profile/AAA")}}>AAA profile</button>
        <button type='button'onClick={()=>{navi("/articles/1")}}>Article</button>
      </header>
      <main>
        {/**중첩된것들 채역오기 */}
        <Outlet/>
      </main>
    </div>
  )
}

export default Layout

import React from 'react'
import { Navigate, useNavigate } from 'react-router-dom'
import {useEffect} from 'react'

const Mypage = () => {
    const isLogin =false
    const navi =useNavigate()
    useEffect(()=>{
        navi("/login")
    },[])

  return (
     <Navigate></Navigate>
  )
}

export default Mypage

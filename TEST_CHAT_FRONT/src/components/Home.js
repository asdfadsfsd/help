import React from 'react'
import { Link } from 'react-router-dom'

const Home = () => {
  return (
    <div>
      <h1>HONE</h1>
      <ul>
        <li>
            <Link to='/introduce'>Introduce</Link>
        </li>
        <li>
            <Link to='/profile/AAA'>AAA Profile</Link>
        </li>
        <li>
            <Link to='/profile/BBB'>BBB Profile</Link>
        </li>
        <li>
            <Link to='/login'>Login</Link>
        </li>
        <li>
            <Link to='/mypage'>My page</Link>
        </li>

      </ul>
    </div>
  )
}

export default Home

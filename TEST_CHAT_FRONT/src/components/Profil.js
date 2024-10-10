import React from 'react'
import { useParams } from 'react-router-dom'

const profiles ={
    AAA :{
        name : "AAA",
        job: "AHAH"
    },
    BBB :{
        name : "VVV",
        job: "VHVH"
    }
}


const Profil = () => {
  const {name} = useParams()
  const params = useParams()
  const profileData = profiles[name]
  return (
    <div>
      {profileData ?
      <div>
        <h1>{profileData.name}</h1>
        <p>{profileData.job}</p>
      </div> :
      <h2>NOT  EXIT</h2>
     }
      
    </div>
  )
}

export default Profil

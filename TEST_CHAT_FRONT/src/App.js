import {Routes, Route} from 'react-router-dom';
import Home from './components/Home';
import Introduce from './components/Introduce';
import Profile from './components/Profil';
import Layout from './components/Layout';
import Articles, {ArticleItem} from './components/Articles';
import Article from './components/Article';
import Login from './components/Login';
import Mypage from './components/Mypage';

function App() {
  return (
    <Routes>
      {/** Routes: 라우팅 기능을 구현하기 위한 최상위 컴포넌트 */}
      {/** 중첩 라우트: Route 컴포넌트 안에 다른 Route 컴포넌트를 추가하여
       *               중첩된 형태로 Route를 구성하는 것. 부모 Route의 element 속성으로 지정된
       *               컴포넌트 안에서 자식 Route의 element 속성으로 지정된 컴포넌트를 
       *               표출할 수 있는 Outlet이라는 컴포넌트를 제공한다.
       */}
       <Route path='/' element={<Layout/>}>
          {/** 각각의 요청 url에 맞는 컴포넌트를 표출하기 위한 Route 컴포넌트 사용
           *    path 속성: 매핑되는 url를 지정하는 속성
           *    element 속성: path 속성으로 지정한 url로 요청이 왔을 때 표출할 컴포넌트 지정하는 속성
           */}
          {/** Route에 index라는 속성이 있는데 index는 path='/'와 동일한 의미이다. */}
          <Route index element={<Home/>}/>
          <Route path='/introduce' element={<Introduce/>}/>
          {/** path 속성에 url 파라미터를 붙일 수 있는데
            *   url파라미터는 콜론(:) + 키변수명 형태로 지정한다.
          */}
          <Route path='/profile/:name' element={<Profile/>}/>
          <Route path='/articles' element={<Articles/>}>
            {/** 중첩된 Route의 자식 Route는 부모의 path 속성도 상속받아서 사용할 수 있다. */}
            <Route path=':id' element={<Article/>}/>
          </Route>
          <Route  path='/login' element={<Login/>} ></Route>
          <Route  path='/mypage' element={<Mypage/>} ></Route>
        </Route>
        
    </Routes>
  );
}

export default App;

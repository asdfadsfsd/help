import React from 'react';
import { NavLink, Outlet } from 'react-router-dom';

// 컴포넌트 정의
export const ArticleItem = ({id}) => {
    const acticleStyle = {
        color: 'green',
        fontSize: 21,
    };

    return (
        <li>
            {/**NavLink: 조건에 맞는 스타일이 적용되는 Link 컴포넌트.
             *           style속성에 함수를 전달한다.
             *           style속성에 전달되는 함수의 매개변수로 활성화여부를 기본적으로 제공한다.
             */}
             <NavLink style={({isActive}) => {return isActive ? acticleStyle : undefined}} to='/articles/{id}'>
                게시글
             </NavLink>
        </li>
    );
}

const Articles = () => {
  return (
    <div>
        <h1>Articles</h1>
        <div>
            <Outlet/>
            <ArticleItem id={1}></ArticleItem>
            <ArticleItem id={2}></ArticleItem>
            <ArticleItem id={3}></ArticleItem>
        </div>
    </div>
  );
};

export default Articles;
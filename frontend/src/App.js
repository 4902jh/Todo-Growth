import React, { useState, useEffect } from 'react';
import CharacterDisplay from './components/CharacterDisplay';
import TodoItem from './components/TodoItem';
import TodoForm from './components/TodoForm';
import './App.css';

function App() {
    const [userId] = useState(1); // 실제로는 로그인한 사용자 ID
    const [currentView, setCurrentView] = useState('main'); // 'main', 'todo-list', 'character', 'add-todo'
    const [todos, setTodos] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        if (currentView === 'todo-list') {
            fetchTodos();
        }
    }, [currentView, userId]);

    const fetchTodos = async () => {
        try {
            setLoading(true);
            const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';
            const response = await fetch(`${API_BASE_URL}/todos/users/${userId}`);
            const data = await response.json();

            if (data.success) {
                setTodos(data.data || []);
            } else {
                console.error('Todo 목록 로드 실패:', data.error);
                setTodos([]);
            }
        } catch (error) {
            console.error('Todo 목록 로드 실패:', error);
            setTodos([]);
        } finally {
            setLoading(false);
        }
    };

    const handleTodoComplete = (data) => {
        console.log('Todo 완료:', data);
        // Todo 목록 새로고침
        fetchTodos();
    };

    const handleTodoFail = (data) => {
        console.log('Todo 실패:', data);
        // Todo 목록 새로고침
        fetchTodos();
    };

    const handleTodoCreated = () => {
        // Todo 생성 후 목록으로 이동
        setCurrentView('todo-list');
        fetchTodos();
    };

    // 메인화면
    if (currentView === 'main') {
        return (
            <div className="App">
                <header className="App-header">
                    <h1>Todo Growth</h1>
                    <p>Todo를 달성하고 캐릭터를 성장시키세요!</p>
                </header>

                <main className="App-main">
                    <div className="main-menu">
                        <button
                            className="menu-button"
                            onClick={() => setCurrentView('add-todo')}
                        >
                            📝 Todo 추가
                        </button>
                        <button
                            className="menu-button"
                            onClick={() => setCurrentView('todo-list')}
                        >
                            📋 Todo 목록
                        </button>
                        <button
                            className="menu-button"
                            onClick={() => setCurrentView('character')}
                        >
                            🎮 캐릭터 상태 보기
                        </button>
                        <button
                            className="menu-button"
                            onClick={() => {
                                if (window.confirm('앱을 종료하시겠습니까?')) {
                                    // 데이터 저장은 자동으로 됨 (백엔드에 저장됨)
                                    alert('앱을 종료합니다.');
                                    // 실제로는 앱 종료 로직
                                }
                            }}
                        >
                            🚪 앱 종료
                        </button>
                    </div>
                </main>
            </div>
        );
    }

    // Todo 추가 화면
    if (currentView === 'add-todo') {
        return (
            <div className="App">
                <header className="App-header">
                    <h1>Todo 추가</h1>
                    <button
                        className="back-button"
                        onClick={() => setCurrentView('main')}
                    >
                        ← 메인으로
                    </button>
                </header>

                <main className="App-main">
                    <TodoForm
                        userId={userId}
                        onTodoCreated={handleTodoCreated}
                        onCancel={() => setCurrentView('main')}
                    />
                </main>
            </div>
        );
    }

    // Todo 목록 화면
    if (currentView === 'todo-list') {
        return (
            <div className="App">
                <header className="App-header">
                    <h1>Todo 목록</h1>
                    <button
                        className="back-button"
                        onClick={() => setCurrentView('main')}
                    >
                        ← 메인으로
                    </button>
                </header>

                <main className="App-main">
                    {loading ? (
                        <p>로딩 중...</p>
                    ) : (
                        <div className="todos-list">
                            {todos.length === 0 ? (
                                <p>등록된 Todo가 없습니다.</p>
                            ) : (
                                todos.map(todo => (
                                    <TodoItem
                                        key={todo.id}
                                        todo={todo}
                                        userId={userId}
                                        onComplete={handleTodoComplete}
                                        onFail={handleTodoFail}
                                    />
                                ))
                            )}
                        </div>
                    )}
                </main>
            </div>
        );
    }

    // 캐릭터 상태 화면
    if (currentView === 'character') {
        return (
            <div className="App">
                <header className="App-header">
                    <h1>캐릭터 상태</h1>
                    <button
                        className="back-button"
                        onClick={() => setCurrentView('main')}
                    >
                        ← 메인으로
                    </button>
                </header>

                <main className="App-main">
                    <div className="character-section">
                        <CharacterDisplay userId={userId} />
                    </div>
                </main>
            </div>
        );
    }

    return null;
}

export default App;
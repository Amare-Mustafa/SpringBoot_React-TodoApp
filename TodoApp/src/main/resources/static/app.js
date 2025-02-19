const API_BASE_URL = '/api';

// DOM Elements
const todoTitleInput = document.getElementById('todoTitle');
const todoDescriptionInput = document.getElementById('todoDescription');
const addTodoBtn = document.getElementById('addTodoBtn');
const todosContainer = document.querySelector('.todos-container');
const filterBtns = document.querySelectorAll('.filter-btn');

let currentFilter = 'uncompleted';

// Event Listeners
addTodoBtn.addEventListener('click', addTodo);
filterBtns.forEach(btn => {
    btn.addEventListener('click', () => {
        const filter = btn.dataset.filter;
        currentFilter = filter;
        filterBtns.forEach(b => b.classList.remove('active'));
        btn.classList.add('active');
        loadTodos();
    });
});

// Load todos on page load
loadTodos();

// Functions
async function loadTodos() {
    try {
        const response = await fetch(`${API_BASE_URL}/todos/${currentFilter}`);
        const todos = await response.json();
        displayTodos(todos);
    } catch (error) {
        console.error('Error loading todos:', error);
    }
}

function displayTodos(todos) {
    todosContainer.innerHTML = '';
    todos.forEach(todo => {
        const todoElement = createTodoElement(todo);
        todosContainer.appendChild(todoElement);
    });
}

function createTodoElement(todo) {
    const div = document.createElement('div');
    div.className = `todo-item ${todo.completed ? 'completed' : ''}`;
    
    const createdAt = new Date(todo.createdAt).toLocaleString();
    
    div.innerHTML = `
        <div class="todo-content">
            <div class="todo-title">${todo.title}</div>
            ${todo.description ? `<div class="todo-description">${todo.description}</div>` : ''}
            <div class="created-at">Created: ${createdAt}</div>
        </div>
        <div class="todo-actions">
            <button onclick="toggleTodoStatus(${todo.id}, ${!todo.completed})">
                <i class="fas ${todo.completed ? 'fa-undo' : 'fa-check'}"></i>
            </button>
            <button class="delete-btn" onclick="deleteTodo(${todo.id})">
                <i class="fas fa-trash"></i>
            </button>
        </div>
    `;
    
    return div;
}

async function addTodo() {
    const title = todoTitleInput.value.trim();
    const description = todoDescriptionInput.value.trim();
    
    if (!title) {
        alert('Please enter a title');
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE_URL}/todos`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                title,
                description,
                completed: false
            })
        });
        
        if (response.ok) {
            todoTitleInput.value = '';
            todoDescriptionInput.value = '';
            loadTodos();
        } else {
            const error = await response.text();
            alert('Error adding todo: ' + error);
        }
    } catch (error) {
        console.error('Error adding todo:', error);
        alert('Error adding todo. Please try again.');
    }
}

async function toggleTodoStatus(id, completed) {
    try {
        const response = await fetch(`${API_BASE_URL}/todos/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ completed })
        });
        
        if (response.ok) {
            loadTodos();
        } else {
            const error = await response.text();
            alert('Error updating todo: ' + error);
        }
    } catch (error) {
        console.error('Error updating todo:', error);
        alert('Error updating todo. Please try again.');
    }
}

async function deleteTodo(id) {
    if (!confirm('Are you sure you want to delete this todo?')) {
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE_URL}/todos/${id}`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            loadTodos();
        } else {
            const error = await response.text();
            alert('Error deleting todo: ' + error);
        }
    } catch (error) {
        console.error('Error deleting todo:', error);
        alert('Error deleting todo. Please try again.');
    }
}

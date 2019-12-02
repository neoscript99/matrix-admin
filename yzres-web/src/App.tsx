import React, { Component } from 'react'
import './App.css'
import { HashRouter as Router, Route, Switch } from 'react-router-dom';
import { Home, Login } from './pages';

class App extends Component {
  render() {
    return (
      <Router>
        <Switch>
          <Route path="/login/" component={Login} />
          <Route component={Home} />
        </Switch>
      </Router>
    )
  }
}

export default App

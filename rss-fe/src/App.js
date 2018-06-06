import React, { Component } from 'react';
import './App.css';
import FeedScreen from "./screen/FeedScreen";
import { CookiesProvider } from 'react-cookie';

class App extends Component {
  render() {
    return (
        <CookiesProvider>
          <div className="App">
              <FeedScreen/>
          </div>
        </CookiesProvider>
    );
  }
}

export default App;

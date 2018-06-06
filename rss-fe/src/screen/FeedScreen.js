import React, { Component } from 'react';
import '../App.css';
import { ListGroup, ListGroupItem, PageHeader, Glyphicon, Modal, Button, Label, Grid, Row, Col, Alert, ButtonToolbar, OverlayTrigger, Tooltip } from 'react-bootstrap';
import { instanceOf } from 'prop-types';
import { withCookies, Cookies } from 'react-cookie';
import { API_ROOT } from '../api-config.js';

class FeedScreen extends Component {

    static propTypes = {
        cookies: instanceOf(Cookies).isRequired
    };

    constructor(props) {
        super(props);

        const { cookies } = this.props;

        this.state = {
            urlMap: cookies.get('urlMap') || [],
            showAdd: false,
            newUrl:"",
            selectedFeed:{},
            selectedFeedUrl:'',
            showItemModal: false,
            selectedItem:{},
            showAlert: false,
            alertMessage: ''
        };
    }

    showItemModal = (item) => {
        this.setState({selectedItem: item}, this.toggleItemModal);
    }

    toggleAddModal = () => {
        this.setState({showAdd: !this.state.showAdd});
    }

    toggleAlertBox = () => {
        this.setState({showAlert: !this.state.showAlert});
    }

    toggleItemModal = () => {
        this.setState({showItemModal: !this.state.showItemModal});
    }

    addNewUrl = () => {
        if (this.state.newUrl !== '') {
            let tempArray = this.state.urlMap;
            tempArray.push(this.state.newUrl);
            this.setState({urlMap: tempArray, newUrl: ''}, this.setRssFeedsCookie())
            this.toggleAddModal();
        }
    }

    removeUrl = (index) => {
        let tempArray = this.state.urlMap;
        tempArray.splice(index, 1);
        this.setState({urlMap: tempArray, selectedFeed: {}}, this.setRssFeedsCookie())
    }

    setRssFeedsCookie = () => {
        const {cookies} = this.props;
        cookies.set('urlMap', this.state.urlMap, { path: '/' });
    }

    componentDidMount() {
        if (this.state.urlMap.length === 0) {
            this.getDefaultUrlList();
        }
    }

    getRssFeed = (mapValue) => {
        fetch(API_ROOT+"/manage-rss/get-feed", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                url: mapValue
            })
        }).then(response => response.json()).then(value => {
            if (value && value.status) {
                this.handleErrorResponse(value);
            } else {
                this.setState({selectedFeed: value,selectedFeedUrl: mapValue, showAlert:false});
            }
        }).catch(error => {
            let errorMessage = '';

            if (error.message) {
                errorMessage = error.message;
            }

            this.setState({alertMessage: 'Unable to connect to back end: ' + errorMessage, showAlert:true});
        });
    }


    handleChange = (event) => {
        this.setState({newUrl: event.target.value});
    }

    handleErrorResponse = (response) => {
        let errorMessage = '';

        if (response.status === 400) {
            errorMessage = response.errors.map((error) => {
                return errorMessage += error.defaultMessage + '\n';
            });
        } else if (response.status === 500) {
            errorMessage = response.message;
        } else {
            errorMessage = 'Error occurred!';
        }

        this.setState({alertMessage: errorMessage, showAlert:true, selectedFeed:{}});
    }

    downloadRssFeed = (url,title) => {
        fetch(API_ROOT+"/manage-rss/download-feed", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify({
                url: url
            })
        }).then(response => response.text()).then(value => {
            if (value && value.status) {
                //this.handleErrorResponse(value);
            } else {
                this.downloadText(value, title);
            }
        }).catch(error => {
            let errorMessage = '';

            if (error.message) {
                errorMessage = error.message;
            }

            this.setState({alertMessage: 'Unable to connect to back end: ' + errorMessage, showAlert:true});
        });
    }

    downloadItem = (item) => {
        fetch(API_ROOT+"/manage-rss/download-item", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify({
                title: item.title,
                link: item.link,
                description: item.description,
                author: item.author,
                thumbnail: item.thumbnail,
                category: item.category,
                enclosure: item.enclosure,
                pubDate: item.pubDate
            })
        }).then(response => response.text()).then(value => {
            if (value && value.status) {
                //this.handleErrorResponse(value);
            } else {
                this.downloadText(value, item.title);
            }
        }).catch(error => {
            let errorMessage = '';

            if (error.message) {
                errorMessage = error.message;
            }

            this.setState({alertMessage: 'Unable to connect to back end: ' + errorMessage, showAlert:true});
        });
    }

    downloadText = (text, title) => {
        var a = document.createElement("a");
        document.body.appendChild(a);
        a.style = "display: none";
        var blob = new Blob([text], {type: "octet/stream"}),
            url = window.URL.createObjectURL(blob);
        a.href = url;
        a.download = title+".txt";
        a.click();
        window.URL.revokeObjectURL(url);
    }

    getDefaultUrlList = () => {
        fetch(API_ROOT+'/manage-rss/get-default-list', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            }
        }).then(response => response.json()).then(value => {
            if (value && value.status) {
                this.handleErrorResponse(value);
            } else {
                let urlMap = [];

                for (var i = 0;  i < value.length; i++) {
                    urlMap.push(value[i])
                }
                this.setState({urlMap:urlMap});
            }
        }).catch(error => {
            let errorMessage = '';

            if (error.message) {
                errorMessage = error.message;
            }

            this.setState({alertMessage: 'Unable to connect to back end: ' + errorMessage, showAlert:true});
        });
    }

    getDate = (timestamp) => {
        var date = new Date(timestamp);
        return date.toLocaleString();
    }

    getShortDescription = (description) => {
        if (description) {
            return description.substring(0, 500).replace(/<\/?.+?>/ig, '');
        } else {
            return "";
        }
    }

    render() {
        const visitWebTooltip = (
            <Tooltip id="visitWebTooltip">
                <strong>Visit website</strong>
            </Tooltip>
        );
        const downloadTooltip = (
            <Tooltip id="downloadTooltip">
                <strong>Download as text</strong>
            </Tooltip>
        );

        return (
            <div>
                <Modal
                    show={this.state.showItemModal}
                    onHide={this.toggleItemModal}
                    container={this}
                    aria-labelledby="contained-modal-title"
                    style={{textAlign:'left'}}
                    bsSize="large">
                    <Modal.Header closeButton>
                        <Modal.Title>
                            <ButtonToolbar>
                                {this.state.selectedItem.title + " "}

                                <OverlayTrigger placement="bottom" overlay={visitWebTooltip}>
                                    <Glyphicon glyph="new-window" className={"MouseHover"} onClick={()=>{
                                        var win = window.open(this.state.selectedItem.link, '_blank');
                                        win.focus();
                                    }}/>
                                </OverlayTrigger>
                                {" "}
                                <OverlayTrigger placement="bottom" overlay={downloadTooltip}>
                                    <Glyphicon glyph="download" className={"MouseHover"} onClick={()=>this.downloadItem(this.state.selectedItem)}/>
                                </OverlayTrigger>
                            </ButtonToolbar>
                        </Modal.Title>
                        <Label>{this.getDate(this.state.selectedItem.pubDate)}</Label>
                    </Modal.Header>

                    <Modal.Body>
                        <div dangerouslySetInnerHTML={{__html: this.state.selectedItem.description}} />
                    </Modal.Body>

                    <Modal.Footer>
                        <Button bsStyle="primary" style={{float:'left'}} href={this.state.selectedItem.link} target={"_blank"}>VISIT WEBSITE</Button>
                        <Button bsStyle="warning" style={{float:'left'}} onClick={()=>this.downloadItem(this.state.selectedItem)}>DOWNLOAD AS TEXT</Button>
                        <Button onClick={this.toggleItemModal}>Close</Button>
                    </Modal.Footer>
                </Modal>
                <Modal
                    show={this.state.showAdd}
                    onHide={this.toggleAddModal}
                    container={this}
                    aria-labelledby="contained-modal-title"
                    bsSize="small"
                >
                    <Modal.Header closeButton>
                        <Modal.Title id="contained-modal-title">
                            Add RSS Feed
                        </Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        RSS Feed: <input type="text" value={this.state.newUrl} onChange={this.handleChange} />
                    </Modal.Body>
                    <Modal.Footer>
                        <Button bsStyle="primary"  onClick={this.addNewUrl}>Add</Button>
                        <Button onClick={this.toggleAddModal}>Close</Button>
                    </Modal.Footer>
                </Modal>
                <PageHeader>
                    RSS Feed Reader
                </PageHeader>
                {this.state.showAlert ?
                    <Alert bsStyle="danger" onDismiss={this.toggleAlertBox}>
                        <h4>Oh snap! You got an error!</h4>
                        <p>
                            {this.state.alertMessage}
                        </p>
                        <p>
                            <Button onClick={this.toggleAlertBox}>Close</Button>
                        </p>
                    </Alert>
                    : null}
                <Grid fluid={true}>
                    <Row className="show-grid">
                        <Col md={2}>
                            <div style={{textAlign: 'left', width: 200}}>
                                <ListGroup>
                                    <ListGroupItem bsStyle="info">
                                        RSS Feed
                                        <div style={{float: 'right'}}>
                                            <Glyphicon glyph="plus" className={"MouseHover"} onClick={()=>this.toggleAddModal()}/>
                                        </div>
                                    </ListGroupItem>
                                    {
                                        this.state.urlMap.map(( mapValue, index ) => {
                                            return (
                                            <ListGroupItem key={index} style={{overflowWrap: 'break-word', overflow: 'auto'}}>
                                                <div className={"MouseHover"} onClick={()=>this.getRssFeed(mapValue)}>{mapValue}</div>
                                                <div  className={"MouseHover"} style={{float: 'right'}}><Glyphicon glyph="minus" onClick={()=>this.removeUrl(index)}/></div>
                                            </ListGroupItem>
                                            );
                                        })
                                    }
                                </ListGroup>
                            </div>
                        </Col>
                        <Col md={10}>
                            <div style={{textAlign: 'left', overflowWrap: 'break-word'}}>
                                {
                                    this.state.selectedFeed &&
                                    this.state.selectedFeed.channel ?
                                        <Button style={{float:'right'}} bsStyle="info" onClick={()=>this.downloadRssFeed(this.state.selectedFeedUrl, this.state.selectedFeed.channel.title)}>
                                            Download As Text File</Button>
                                        :null
                                }
                                <ListGroup>
                                    {
                                        this.state.selectedFeed &&
                                        this.state.selectedFeed.channel &&
                                        this.state.selectedFeed.channel.items ? this.state.selectedFeed.channel.items.map((item, index) => {
                                            return (<ListGroupItem key={index} onClick={()=>this.showItemModal(item)}>
                                                <div>
                                                    <h3>
                                                        {item.title}
                                                    </h3>
                                                    <p>{this.getShortDescription(item.description)}</p>
                                                    <ul className="list-inline list-unstyled">
                                                        <li><span><i className="glyphicon glyphicon-calendar"></i> {this.getDate(item.pubDate)} </span>
                                                        </li>
                                                        <li>|</li>
                                                        <li>
                                                            <span><i className="glyphicon glyphicon-user"></i> {item.author} </span>
                                                        </li>
                                                        <li>|</li>
                                                        <li>
                                                            <span><i className="glyphicon glyphicon-file"></i> {this.state.selectedFeed.channel.title} </span>
                                                        </li>
                                                    </ul>
                                                </div>
                                            </ListGroupItem>)
                                        }) : <h4 style={{textAlign: 'center'}}>Select RSS Feed from the left menu</h4>
                                    }
                                </ListGroup>
                            </div>
                        </Col>
                    </Row>
                </Grid>
            </div>
        );
    }
}
export default withCookies(FeedScreen);

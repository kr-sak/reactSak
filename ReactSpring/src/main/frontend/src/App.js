import React, {useState, useEffect} from "react";
import axios from "axios";

const App = () => {

	const [posts, setPosts] = useState([]);

	useEffect (() => {
		// axios({
		// 	method: 'GET',
		// 	url:'https://jsonplaceholder.typicode.com/photos'
		// }).then(response => setPosts(response.data))

		// axios.get('https://jsonplaceholder.typicode.com/photos')
		// 	 .then(response => setPosts(response.data))
		const fetchData = async () => {
		try {
			const response = await axios.get('https://jsonplaceholder.typicode.com/photos');
			setPosts(response.data);
		} catch (error) {
			console.log(error);
		}
	}
	fetchData().catch(console.error)
	})

	return (
		<ul>
			{posts.map(post => (
				<li key = {post.id}>
				<div>{post.title}</div>
				<div><img src = {post.thumbnailUrl}></img></div>
				</li>
			))}
		</ul>
	)
}

export default App;

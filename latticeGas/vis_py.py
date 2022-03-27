from vispy import scene, app


def plot(grid_size,data):
    num_particles = len(data[0])
    canvas = scene.SceneCanvas(title='lattice gas simulation', size=(grid_size, grid_size),
                            bgcolor='white', show=True)

    view = canvas.central_widget.add_view()


    particles = [scene.visuals.Ellipse(center=(0,0),radius=10, parent=view.scene) for i in range(num_particles)]


    frame_number = 0

    def update():
        global frame_number
        print(frame_number)
        for i in range(num_particles):
            x, y, dir = data[frame_number][i]
            particles[i].center = (x, y)
        frame_number +=1


    timer = app.Timer(interval=20, connect=update)
    timer.start()


    app.run()
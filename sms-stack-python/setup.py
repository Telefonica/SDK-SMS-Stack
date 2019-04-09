import setuptools

s = [
    'pytest'
]

with open("README.md", "r") as fh:
    long_description = fh.read()

setuptools.setup(
    name="sms-stack",
    version="1.1.0",
    author="Lucas Fernandez",
    author_email="lucas.fernandezaragon@telefonica.com",
    description="Sms Stack SDK for python",
    long_description=long_description,
    long_description_content_type="text/markdown",
    url="",
    keywords=[
        'sms-stack',
        'tcp',
        'protocol'
    ],
    packages=setuptools.find_packages(),
    classifiers=[
        "Programming Language :: Python :: 3",
        "License :: OSI Approved :: MIT License",
        "Operating System :: OS Independent",
    ],
)